package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.ImmutableProject
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger

class OfflineProjectsDatabaseTest {

    private val projectsDir = Files.createTempDirectory("offline_test").toFile()

    private val p1 = ImmutableProject("12345","What fraction of Google searches are answered by Wikipedia?","DLAB","Robert West","TA1",1, listOf<String>(),false,true, listOf("data analysis","large datasets","database","systems","database","systems"),false,"Description of project1")
    private val p2 = ImmutableProject("11111","Real-time reconstruction of deformable objects","CVLAB","Teacher2","TA2",1, listOf<String>(),false,true, listOf("Computer Vision","ML"),false,"Description of project2")
    private val p3 = ImmutableProject("00000","Implement a fast driver for a 100 Gb/s network card","DSLAB","Teacher5","TA5",3, listOf<String>(),false,true, listOf("Low Level","Networking","Driver"),false,"Description of project5")

    private val onFailureNotExpected: ((Exception) -> Unit) = { e ->
        e.printStackTrace()
        assertNull(e)
        assertTrue(false)
    }


    @Before
    fun setup() {
        projectsDir.mkdirs()

        val validDir = File(projectsDir, "testDir55555")
        validDir.mkdirs()

        val invalidDir = File(projectsDir, "dir invalid !")
        invalidDir.mkdirs()

        val invalidFile = File(validDir, "project.data")
        invalidFile.mkdir()

        val validDir2 = File(projectsDir, "validDirWithInvalidProject")
        validDir2.mkdirs()

        val invalidFile2 = File(validDir2, "project.data")
        invalidFile2.writeText("This is obviously not a Map serialized")
    }

    @After
    fun clean() {
        projectsDir.deleteRecursively()
    }

    @Test(timeout = 4000)
    fun getAllIdsWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)
        fakeDB.modify(p3)
        Thread.sleep(100)

        val emptyDB = FakeDatabaseTest(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result: List<ProjectId>? = null
        db2.getAllIds({
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals(listOf(p2.id, p3.id).sorted(), result!!.sorted())
    }

    @Test(timeout = 4000)
    fun getAllProjectsWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeDatabaseTest(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result: List<ImmutableProject>? = null
        db2.getAllProjects({
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals(listOf(p2, p3).sortedBy { p -> p.id }, result!!.sortedBy { p -> p.id })
    }

    @Test(timeout = 4000)
    fun getProjectFromIdWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeDatabaseTest(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result1: ImmutableProject? = null
        db2.getProjectFromId(p2.id, {
            result1 = it
        }, onFailureNotExpected)
        while (result1 == null);
        assertEquals(p2, result1)

        var result2: ImmutableProject? = null
        db2.getProjectFromId(p1.id, {
            result2 = it
        }, onFailureNotExpected)
        Thread.sleep(100)
        assertEquals(null, result2)
    }

    @Test(timeout = 4000)
    fun getProjectsFromNameWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeDatabaseTest(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result1: List<ImmutableProject>? = null
        db2.getProjectsFromName(p2.name, {
            result1 = it
        }, onFailureNotExpected)
        while (result1 == null);
        assertEquals(listOf(p2), result1)

        var result2: List<ImmutableProject>? = null
        db2.getProjectsFromName(p1.name, {
            result2 = it
        }, onFailureNotExpected)
        while (result2 == null);
        assertEquals(0, result2!!.size)
    }

    @Test(timeout = 4000)
    fun getProjectsFromTagsWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeDatabaseTest(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result: List<ImmutableProject>? = null
        db2.getProjectsFromTags(listOf("ml", "database"), {
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals(listOf(p2), result)

    }


    @Test(timeout = 4000)
    fun pushIsCorrectlyCalled() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)

        var result: ProjectId? = null
        db.pushProject(p3, {
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals("90000", result)
        assertEquals(3, fakeDB.projects.size)
    }

    @Test(timeout = 4000)
    fun deleteProjectWithIdIsCorrectlyCalled() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)

        db.deleteProjectWithId(p2.id, {}, onFailureNotExpected)
        Thread.sleep(100)
        assertEquals(listOf(p1), fakeDB.projects)
    }

    @Test(timeout = 4000)
    fun updateVideoWithProjectIsCorrectlyCalled() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)

        val uriString = "fakeUriVideo"

        db.updateVideoWithProject(p1.id, uriString, {}, onFailureNotExpected)
        Thread.sleep(100)
        assertEquals(fakeDB.lastUpdate, p1.id to uriString)
    }

    @Test(timeout = 4000)
    fun listenersWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeDatabaseTest(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        var count: AtomicInteger = AtomicInteger(0)

        val listener: ((ProjectChange) -> Unit) = { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> assertEquals(p3, change.project)
                ProjectChange.Type.MODIFIED -> assertEquals(p2, change.project)
                ProjectChange.Type.REMOVED -> assertEquals(p2, change.project)
            }
            count.incrementAndGet()
            change.project
        }

        db.addProjectsChangeListener(listener)

        fakeDB.modify(p2)
        fakeDB.remove(p2)
        fakeDB.remove(p2)
        fakeDB.add(p3)

        Thread.sleep(500)
        assertEquals(4, count.get())

        db.removeProjectsChangeListener(listener)
        fakeDB.modify(p1)
        fakeDB.remove(p1)
        fakeDB.remove(p1)
        fakeDB.add(p1)

        Thread.sleep(100)
        assertEquals(4, count.get())
    }
}