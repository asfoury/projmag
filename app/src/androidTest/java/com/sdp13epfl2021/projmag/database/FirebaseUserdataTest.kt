package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseUserdataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase

@Suppress("UNCHECKED_CAST")
class FirebaseUserdataTest {
    companion object {
        private const val ID = "some-project-id"
        private const val UID = "some-user-id"
    }


    private val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    private val mockFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    private val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)

    private val mockColRef = Mockito.mock(CollectionReference::class.java)
    private val mockDocRef = Mockito.mock(DocumentReference::class.java)
    private val mockDS = Mockito.mock(DocumentSnapshot::class.java) as DocumentSnapshot

    private val mockVoidTask: Task<Void> = Mockito.mock(Task::class.java) as Task<Void>
    private val mockDSTask: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>

    private val database: UserdataDatabase =
        FirebaseUserdataDatabase(mockFirebaseFirestore, mockFirebaseAuth)


    @Before
    fun setupMocks() {
        // ColRef
        Mockito
            .`when`(mockFirebaseFirestore.collection(FirebaseUserdataDatabase.ROOT))
            .thenReturn(mockColRef)
        Mockito
            .`when`(mockColRef.document(UID))
            .thenReturn(mockDocRef)

        //DocRef
        Mockito
            .`when`(
                mockDocRef.set(
                    JavaToKotlinHelperAndroidTest.anyObject(),
                    JavaToKotlinHelperAndroidTest.anyObject()
                )
            )
            .thenReturn(mockVoidTask)
        Mockito
            .`when`(mockDocRef.get())
            .thenReturn(mockDSTask)
        Mockito
            .`when`(
                mockDocRef.update(
                    Mockito.anyString(),
                    JavaToKotlinHelperAndroidTest.anyObject()
                )
            )
            .thenReturn(mockVoidTask)

        //DocumentSnapshot
        Mockito
            .`when`(mockDS[FirebaseUserdataDatabase.FAVORITES_FIELD])
            .thenReturn(listOf(ID))


        //FirebaseAuth
        Mockito
            .`when`(mockFirebaseAuth.currentUser)
            .thenReturn(mockFirebaseUser)

        //FirebaseUser
        Mockito
            .`when`(mockFirebaseUser.uid)
            .thenReturn(UID)

        //Task<Void>
        Mockito
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<Void>
                osl?.onSuccess(null)
                mockVoidTask
            }
        Mockito
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .thenReturn(mockVoidTask)

        //Task<DocumentSnapshot>
        Mockito
            .`when`(mockDSTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<DocumentSnapshot>
                osl?.onSuccess(mockDS)
                mockVoidTask
            }

    }

    @Test
    fun pushFavoriteProjectDoNotCrash() {
        /* It also tests the list version */
        database.pushFavoriteProject(
            ID,
            {},
            {}
        )
    }

    @Test
    fun getListOfFavoriteProjectsIsCorrect() {
        database.getListOfFavoriteProjects(
            { list -> Assert.assertEquals(listOf(ID), list) },
            {}
        )
    }

    @Test
    fun removeFromFavoriteDoNotCrash() {
        database.removeFromFavorite(
            ID,
            {},
            {}
        )
    }
}