package com.sdp13epfl2021.projmag.database


import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest
import com.sdp13epfl2021.projmag.database.impl.firebase.UserProfileDatabase
import com.sdp13epfl2021.projmag.model.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserProfileDatabaseTest {
    private val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    private val mockColRef = Mockito.mock(CollectionReference::class.java)
    private val mockFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    private val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)
    private val mockDS: DocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    private val mockDocRef = Mockito.mock(DocumentReference::class.java)
    private val mockTaskDoc: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>
    private val mockVoidTask: Task<Void> = Mockito.mock(Task::class.java) as Task<Void>
    private val mockDSTask: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>

    private val currentUser = "user1"
    private val uid = "uid123456"

    private val exampleUserProfile = when (val prof = ImmutableProfile.build(
        "Last Name",
        "First Name",
        22,
        Gender.MALE,
        289982,
        "01001001",
        Role.STUDENT
    )) {
        is Success -> prof.value
        is Failure -> null
    }

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setupMocks() {
        Mockito
            .`when`(mockFirebaseFirestore.collection(UserProfileDatabase.ROOT))
            .thenReturn(mockColRef)

        Mockito
            .`when`(mockColRef.document(currentUser))
            .thenReturn(mockDocRef)

        Mockito
            .`when`(mockFirebaseAuth.currentUser)
            .thenReturn(mockFirebaseUser)

        Mockito
            .`when`(mockFirebaseUser.uid)
            .thenReturn(currentUser)

        Mockito
            .`when`(mockDocRef.get())
            .thenReturn(mockTaskDoc)

        // --- mockTaskDoc ---
        Mockito
            .`when`(mockTaskDoc.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<DocumentSnapshot>
                a.onSuccess(mockDS)
                mockTaskDoc
            }

        Mockito
            .`when`(mockTaskDoc.addOnFailureListener { Mockito.any(OnFailureListener::class.java) })
            .thenReturn(mockTaskDoc)

        // -- mock set --
        Mockito
            .`when`(mockDocRef.set(JavaToKotlinHelperAndroidTest.anyObject()))
            .thenReturn(mockVoidTask)

        //Task<Void>
        Mockito
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<Void>
                osl?.onSuccess(null)
                mockVoidTask
            }

        Mockito
            .`when`(mockVoidTask.addOnFailureListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .thenReturn(mockVoidTask)

        Mockito.`when`(mockDS["firstName"]).thenReturn(exampleUserProfile?.firstName)
        Mockito.`when`(mockDS["lastName"]).thenReturn(exampleUserProfile?.lastName)
        Mockito.`when`(mockDS["age"]).thenReturn(exampleUserProfile?.age?.toLong())
        Mockito.`when`(mockDS["gender"]).thenReturn(exampleUserProfile?.gender?.name)
        Mockito.`when`(mockDS["sciper"]).thenReturn(exampleUserProfile?.sciper?.toLong())
        Mockito.`when`(mockDS["phoneNumber"]).thenReturn(exampleUserProfile?.phoneNumber)
        Mockito.`when`(mockDS["role"]).thenReturn(exampleUserProfile?.role?.name)

    }

    @Test
    fun checkThatGettingProfileWorks() {
        val udb = UserProfileDatabase(mockFirebaseFirestore, mockFirebaseAuth)
        udb.getProfile({
            assertEquals(exampleUserProfile?.firstName, it?.firstName)
            assertEquals(exampleUserProfile?.lastName, it?.lastName)
            assertEquals(exampleUserProfile?.age, it?.age)
            assertEquals(exampleUserProfile?.sciper, it?.sciper)
            assertEquals(exampleUserProfile?.phoneNumber, it?.phoneNumber)
            assertEquals(exampleUserProfile?.role, it?.role)
            assertEquals(exampleUserProfile?.gender, it?.gender)


        }, {
            Log.d("TEST", it.toString())
        })
    }

    @Test
    fun checkThatUploadingProfileWorks() {
        val udb = UserProfileDatabase(mockFirebaseFirestore, mockFirebaseAuth)
        if (exampleUserProfile != null) {
            udb.uploadProfile(exampleUserProfile, {}, {})
        }
    }


}