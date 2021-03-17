package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.lang.Exception
import java.util.function.Consumer


/**
 *  Simple tests as this is just an abstraction of the
 *  Firebase/Firestore API
 */
@Suppress("UNCHECKED_CAST")
class FirebaseProjectsDatabaseTest {
    val ID = "some-id"
    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    val mockColRef = Mockito.mock(CollectionReference::class.java)
    val mockTask: Task<QuerySnapshot> = Mockito.mock(Task::class.java) as Task<QuerySnapshot>
    val mockQS: QuerySnapshot = Mockito.mock(QuerySnapshot::class.java)
    val mockIterator: MutableIterator<QueryDocumentSnapshot> =
        Mockito.mock(Iterator::class.java) as MutableIterator<QueryDocumentSnapshot>
    val mockQDS: QueryDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot::class.java)

    private fun newQDSIterator() = object : MutableIterator<QueryDocumentSnapshot> {
        private var nb = 1

        override fun hasNext(): Boolean = nb > 0

        override fun next(): QueryDocumentSnapshot {
            nb -= 1
            return mockQDS
        }

        override fun remove() {}

    }

    @Before
    fun setUpMocks() {
        Mockito
            .`when`(mockFirebaseFirestore.collection(FirebaseProjectsDatabase.ROOT))
            .thenReturn(mockColRef)

        Mockito
            .`when`(mockColRef.get())
            .thenReturn(mockTask)

        Mockito
            .`when`(mockTask.addOnSuccessListener(anyObject()))
            .then {
                val a = it.arguments[0] as OnSuccessListener<QuerySnapshot>
                a.onSuccess(mockQS)
                mockTask
            }

        Mockito
            .`when`(mockTask.addOnFailureListener { Mockito.any(OnFailureListener::class.java) })
            .thenReturn(mockTask)

        Mockito
            .`when`(mockQS.iterator())
            .thenReturn(newQDSIterator())

        Mockito
            .`when`(mockQDS.id)
            .thenReturn(ID)
    }

    private fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    /**
     * test that no unexpected behaviour when passing null
     */
    @Test
    fun pushNullProjectShouldNotCrash() {
        val db = FirebaseProjectsDatabase(mockFirebaseFirestore)
        db.pushProject(null, {}, {})
    }

    @Test
    fun getAllIdsIsCorrect() {
        val db = FirebaseProjectsDatabase(mockFirebaseFirestore)
        db.getAllIds(
            { list -> assertEquals(listOf(ID), list) },
            {}
        )
    }
}