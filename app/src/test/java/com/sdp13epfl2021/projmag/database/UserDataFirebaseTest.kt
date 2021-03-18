package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
class UserDataFirebaseTest {
    val ID = "some-project-id"
    val UID = "some-user-id"

    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    val mockFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)

    val mockColRef = Mockito.mock(CollectionReference::class.java)
    val mockDocRef = Mockito.mock(DocumentReference::class.java)
    val mockDS = Mockito.mock(DocumentSnapshot::class.java) as DocumentSnapshot

    val mockVoidTask: Task<Void> = Mockito.mock(Task::class.java) as Task<Void>
    val mockDSTask: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>

    val database = UserDataFirebase(mockFirebaseFirestore, mockFirebaseAuth)

    /**
     * Workaround found on [StackOverflow][https://stackoverflow.com/a/30308199] to avoid
     * a `NullPointerException` caused by Java to Kotlin type cast
     */
    object JavaToKotlinHelper {
        fun <T> anyObject(): T {
            Mockito.anyObject<T>()
            return uninitialized()
        }

        private fun <T> uninitialized(): T = null as T
    }

    @Before
    fun setupMocks() {
        // ColRef
        Mockito
            .`when`(mockFirebaseFirestore.collection(UserDataFirebase.ROOT))
            .thenReturn(mockColRef)
        Mockito
            .`when`(mockColRef.document(UID))
            .thenReturn(mockDocRef)

        //DocRef
        Mockito
            .`when`(mockDocRef.set(JavaToKotlinHelper.anyObject(), JavaToKotlinHelper.anyObject()))
            .thenReturn(mockVoidTask)
        Mockito
            .`when`(mockDocRef.get())
            .thenReturn(mockDSTask)
        Mockito
            .`when`(mockDocRef.update(Mockito.anyString(), JavaToKotlinHelper.anyObject()))
            .thenReturn(mockVoidTask)

        //DocumentSnapshot
        Mockito
            .`when`(mockDS[UserDataFirebase.FAVORITES_FIELD])
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
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelper.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<Void>
                osl?.onSuccess(null)
                mockVoidTask
            }
        Mockito
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelper.anyObject()))
            .thenReturn(mockVoidTask)

        //Task<DocumentSnapshot>
        Mockito
            .`when`(mockDSTask.addOnSuccessListener(JavaToKotlinHelper.anyObject()))
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