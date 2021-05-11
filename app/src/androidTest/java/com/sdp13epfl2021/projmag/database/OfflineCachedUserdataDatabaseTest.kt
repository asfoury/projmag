package com.sdp13epfl2021.projmag.database

import com.google.common.io.Files
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.fake.FakeUserdataDatabase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedUserdataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import junit.framework.TestCase.*
import org.junit.Test
import java.io.File
import java.util.concurrent.CompletableFuture

class OfflineCachedUserdataDatabaseTest {


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


    private val onFailureNotExpected: (Exception) -> Unit = {
        assert(false)
    }


    @Test(timeout = 1000)
    fun cvWork() {
        val tempDir: File = Files.createTempDir()
        val fakeDB = FakeUserdataDatabase(userID)
        val db1: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)

        db1.pushCv(cv, {}, onFailureNotExpected)

        val result1: CompletableFuture<CurriculumVitae> = CompletableFuture()
        db1.getCv(userID, {
            result1.complete(it)
        }, onFailureNotExpected)
        assertEquals(cv, result1.get())

        val db2: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)
        val result2: CompletableFuture<CurriculumVitae> = CompletableFuture()
        db1.getCv(userID, {
            result2.complete(it)
        }, onFailureNotExpected)
        assertEquals(cv, result2.get())

        val result3: CompletableFuture<CurriculumVitae?> = CompletableFuture()
        db1.getCv("333", {
            result3.complete(it)
        }, onFailureNotExpected)
        assertNull(result3.get())

        tempDir.deleteRecursively()
    }

    @Test(timeout = 1000)
    fun favoritesWork() {
        val tempDir: File = Files.createTempDir()
        val fakeDB = FakeUserdataDatabase(userID)
        val db1: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)
        val expected: List<ProjectId> = listOf("1", "2", "4").sorted()

        db1.pushFavoriteProject("1", {}, onFailureNotExpected)
        db1.pushFavoriteProject("1", {}, onFailureNotExpected)
        db1.pushFavoriteProject("3", {}, onFailureNotExpected)
        db1.removeFromFavorite("2", {}, onFailureNotExpected)
        db1.removeFromFavorite("3", {}, onFailureNotExpected)
        db1.pushListOfFavoriteProjects(listOf("2", "4"), {}, onFailureNotExpected)

        val future1: CompletableFuture<List<ProjectId>> = CompletableFuture()
        db1.getListOfFavoriteProjects({
            future1.complete(it)
        }, onFailureNotExpected)
        assertEquals(expected, future1.get().sorted())

        val db2: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)
        val future2: CompletableFuture<List<ProjectId>> = CompletableFuture()
        db2.getListOfFavoriteProjects({
            future2.complete(it)
        }, onFailureNotExpected)
        assertEquals(expected, future2.get().sorted())

        tempDir.deleteRecursively()
    }

    @Test(timeout = 1000)
    fun appliedWork() {
        val tempDir: File = Files.createTempDir()
        val fakeDB = FakeUserdataDatabase(userID)
        val db1: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)
        val expected: List<ProjectId> = listOf("2")

        db1.applyUnapply(true, "1", {}, onFailureNotExpected)
        db1.applyUnapply(true, "2", {}, onFailureNotExpected)
        db1.applyUnapply(true, "2", {}, onFailureNotExpected)
        db1.applyUnapply(false, "1", {}, onFailureNotExpected)

        val future1: CompletableFuture<List<ProjectId>> = CompletableFuture()
        db1.getListOfAppliedToProjects({
            future1.complete(it)
        }, onFailureNotExpected)
        assertEquals(expected, future1.get())


        val db2: UserdataDatabase = OfflineCachedUserdataDatabase(fakeDB, userID, tempDir)
        val future2: CompletableFuture<List<ProjectId>> = CompletableFuture()
        db2.getListOfAppliedToProjects({
            future2.complete(it)
        }, onFailureNotExpected)
        assertEquals(expected, future2.get())

        tempDir.deleteRecursively()
    }

}