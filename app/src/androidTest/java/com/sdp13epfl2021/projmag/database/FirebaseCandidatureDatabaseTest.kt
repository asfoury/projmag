package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.fake.FakeUserdataDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseCandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.CompletableFuture

class FirebaseCandidatureDatabaseTest {

    companion object {
        private const val PID = "some-project-id"
        private const val UID = "some-user-id"
    }

    private val mockFirebaseAuthFailed = Mockito.mock(FirebaseAuth::class.java)
    private val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    private val mockFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    private val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)

    private val mockColRef = Mockito.mock(CollectionReference::class.java)
    private val mockDocRef = Mockito.mock(DocumentReference::class.java)
    private val mockDS = Mockito.mock(DocumentSnapshot::class.java) as DocumentSnapshot

    private val mockVoidTask: Task<Void> = Mockito.mock(Task::class.java) as Task<Void>
    private val mockDSTask: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>

    val userIDRejected = "001"
    val userIDAccepted = "002"
    val userIDWaiting = "003"
    val userIDInvalidType = "004"
    val userIDEmpty = "005"

    private val userDB: UserdataDatabase = FakeUserdataDatabase(
        profiles = hashMapOf(
            userIDRejected to dummyProfile(userIDRejected),
            userIDAccepted to dummyProfile(userIDAccepted),
            userIDWaiting to dummyProfile(userIDWaiting),
            userIDInvalidType to dummyProfile(userIDInvalidType),
            userIDEmpty to dummyProfile(userIDEmpty),
        ),
        cvs = hashMapOf(
            userIDRejected to dummyCv(userIDRejected),
            userIDAccepted to dummyCv(userIDAccepted),
            userIDWaiting to dummyCv(userIDWaiting),
            userIDInvalidType to dummyCv(userIDInvalidType),
            userIDEmpty to dummyCv(userIDEmpty),
        )
    )
    private val candidatureDB =
        FirebaseCandidatureDatabase(mockFirebaseFirestore, mockFirebaseAuth, userDB)
    private val candidatureDBWithoutAuth =
        FirebaseCandidatureDatabase(mockFirebaseFirestore, mockFirebaseAuthFailed, userDB)



    val onFailureNotExpected: (Exception) -> Unit = { e: Exception ->
        assertTrue(false)
    }

    val candidature: Candidature = Candidature(
        PID,
        UID,
        (ImmutableProfile.build(
            "lastName2",
            "firstName2",
            21,
            Gender.MALE,
            123456,
            "021 123 45 67", Role.STUDENT
        ) as Success).value,
        CurriculumVitae(
            "summary 2",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        ),
        Candidature.State.Waiting
    )

    private fun dummyProfile(userId: String): ImmutableProfile {
        return (ImmutableProfile.build(
            "lastName_$userId",
            "firstName_$userId",
            21,
            Gender.MALE,
            123456,
            "021 123 45 67", Role.STUDENT
        ) as Success).value
    }

    private fun dummyCv(userId: String): CurriculumVitae {
        return CurriculumVitae(
            "summary of $userId",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    @Before
    fun setupMocks() {
        // ColRef
        Mockito
            .`when`(mockFirebaseFirestore.collection(FirebaseCandidatureDatabase.ROOT))
            .thenReturn(mockColRef)
        Mockito
            .`when`(mockColRef.document(FirebaseCandidatureDatabaseTest.PID))
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

        //DocumentSnapshot
        Mockito
            .`when`(mockDS.exists())
            .thenReturn(true)

        Mockito
            .`when`(mockDS.data)
            .thenReturn(
                mapOf(
                    userIDAccepted to "Accepted",
                    userIDRejected to "Rejected",
                    userIDWaiting to "Waiting",
                    userIDInvalidType to 300,
                    userIDEmpty to null
                )
            )


        //FirebaseAuth
        Mockito
            .`when`(mockFirebaseAuth.currentUser)
            .thenReturn(mockFirebaseUser)

        //FirebaseUser
        Mockito
            .`when`(mockFirebaseUser.uid)
            .thenReturn(FirebaseCandidatureDatabaseTest.UID)

        //Task<Void>
        Mockito
            .`when`(mockVoidTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<Void>
                osl?.onSuccess(null)
                mockVoidTask
            }

        //Task<DocumentSnapshot>
        Mockito
            .`when`(mockDSTask.addOnSuccessListener(JavaToKotlinHelperAndroidTest.anyObject()))
            .then {
                val osl = it.arguments[0] as? OnSuccessListener<DocumentSnapshot>
                osl?.onSuccess(mockDS)
                mockVoidTask
            }


    }


    @Test(timeout = 1000)
    fun getListOfCandidaturesWorks() {
        val future: CompletableFuture<List<Candidature>> = CompletableFuture()
        candidatureDB.getListOfCandidatures(PID, {
            future.complete(it)
        }, onFailureNotExpected)

        val result: List<Candidature> = future.get()

        assertEquals(3, result.size)
        assertTrue(result.all { c -> c.projectId == PID })
        val usersID = result.map { c -> c.userId }
        assertTrue(usersID.contains(userIDAccepted))
        assertTrue(usersID.contains(userIDRejected))
        assertTrue(usersID.contains(userIDWaiting))
    }

    @Test(timeout = 1000)
    fun acceptAndRejectAndPushCompleteSuccessfully() {
        val result1: CompletableFuture<Int> = CompletableFuture()
        val result2: CompletableFuture<Int> = CompletableFuture()
        val result3: CompletableFuture<Int> = CompletableFuture()

        candidatureDB.pushCandidature(
            candidature.projectId,
            candidature.userId,
            Candidature.State.Accepted,
            {
                result1.complete(1)
            },
            onFailureNotExpected
        )

        candidatureDB.pushCandidature(
            candidature.projectId,
            candidature.userId,
            Candidature.State.Accepted,
            {
                result2.complete(2)
            },
            onFailureNotExpected
        )

        candidatureDB.pushCandidature(
            candidature.projectId,
            candidature.userId,
            Candidature.State.Waiting,
            {
                result3.complete(3)
            },
            onFailureNotExpected
        )

        assertEquals(1, result1.get())
        assertEquals(2, result2.get())
        assertEquals(3, result3.get())
    }

}