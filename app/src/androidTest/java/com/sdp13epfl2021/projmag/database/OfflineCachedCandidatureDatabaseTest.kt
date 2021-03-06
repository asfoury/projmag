package com.sdp13epfl2021.projmag.database

import com.google.common.io.Files
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.fake.FakeCandidatureDatabase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedCandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.util.concurrent.CompletableFuture

class OfflineCachedCandidatureDatabaseTest {

    private val mockInvalidDir1: File = Mockito.mock(File::class.java)
    private val mockInvalidDir2: File = Mockito.mock(File::class.java)

    private val projectId: ProjectId = "001"
    private val userID = "fakeUserID"

    private val cv: CurriculumVitae = CurriculumVitae(
        "summary",
        listOf(
            CurriculumVitae.PeriodDescription(
                "name",
                "loc",
                "description",
                2010,
                2020
            )
        ),
        listOf(),
        listOf(),
        listOf()
    )
    private val profile: ImmutableProfile = (ImmutableProfile.build(
        "lastname",
        "firstname",
        20,
        Gender.OTHER,
        123456,
        "021 123 45 67",
        Role.OTHER
    ) as Success<ImmutableProfile>).value


    private val onFailureNotExpected: (Exception) -> Unit = {
        assert(false)
    }

    @Before
    fun setup() {
        Mockito
            .`when`(mockInvalidDir1.listFiles())
            .thenReturn(null)

        Mockito
            .`when`(mockInvalidDir2.listFiles())
            .then { throw SecurityException("not readable") }
    }

    @Test
    fun candidatureWork() {
        val tempDir: File = Files.createTempDir()
        val waitingCandidature =
            Candidature(projectId, userID, profile, cv, Candidature.State.Waiting)
        val acceptedCandidature =
            Candidature(projectId, userID, profile, cv, Candidature.State.Accepted)
        val fakeDB =
            FakeCandidatureDatabase(candidatures = hashMapOf(projectId to hashMapOf(userID to waitingCandidature)))
        val db1: CandidatureDatabase = OfflineCachedCandidatureDatabase(fakeDB, tempDir)

        val future1: CompletableFuture<List<Candidature>> = CompletableFuture()
        db1.getListOfCandidatures(projectId, {
            future1.complete(it)
        }, onFailureNotExpected)
        assertEquals(listOf(waitingCandidature), future1.get())

        val future0: CompletableFuture<Int> = CompletableFuture()
        db1.pushCandidature(projectId, userID, Candidature.State.Accepted, {
            future0.complete(0)
        }, onFailureNotExpected)
        future0.get()

        val db2: CandidatureDatabase = OfflineCachedCandidatureDatabase(fakeDB, tempDir)

        val future2: CompletableFuture<List<Candidature>> = CompletableFuture()
        db2.getListOfCandidatures(projectId, {
            future2.complete(it)
        }, onFailureNotExpected)
        assertEquals(listOf(acceptedCandidature), future2.get())


        val emptyDB: CandidatureDatabase = FakeCandidatureDatabase()
        val db3: CandidatureDatabase = OfflineCachedCandidatureDatabase(emptyDB, tempDir)

        val future3: CompletableFuture<List<Candidature>> = CompletableFuture()
        db3.getListOfCandidatures(projectId, {
            future3.complete(it)
        }, onFailureNotExpected)
        assertEquals(listOf(acceptedCandidature), future3.get())

        val future4: CompletableFuture<Int> = CompletableFuture()
        db3.removeCandidature(projectId, userID, {
            future4.complete(0)
        }, onFailureNotExpected)
        future4.get()

        val future5: CompletableFuture<List<Candidature>> = CompletableFuture()
        db3.getListOfCandidatures(projectId, {
            future5.complete(it)
        }, onFailureNotExpected)
        assertEquals(emptyList<Candidature>(), future5.get())

        tempDir.deleteRecursively()
    }

    @Test
    fun invalidDirShouldNotCrash() {
        val fakeDB = FakeCandidatureDatabase()

        OfflineCachedCandidatureDatabase(fakeDB, mockInvalidDir1)
        OfflineCachedCandidatureDatabase(fakeDB, mockInvalidDir2)
    }
}