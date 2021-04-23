package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.database.fake.FakeProjectsDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import com.sdp13epfl2021.projmag.model.Tag
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger

class OfflineProjectsDatabaseTest {

    private val projectsDir = Files.createTempDirectory("offline_test").toFile()
    private val unreadableDir = Files.createTempDirectory("offline_test_invalid").toFile()

    private val tagManager = TagsBaseManager()
    private val sectionManager = SectionBaseManager()
    private val tagList : List<String> = tagManager.tagsListToStringList(tagManager.getAllTags())
    private val sectionList : List<String> = sectionManager.sectionList()
    private val p1 = ImmutableProject(
        "12345",
        "What fraction of Google searches are answered by Wikipedia?",
        "DLAB",
        "Robert West",
        "TA1",
        1,
        listOf<String>(),
        false,
        true,
        listOf(tagList[0], tagList[3]),
        false,
        "Description of project1",
        listOf(),
        listOf(sectionList[0])
    )
    private val p2 = ImmutableProject(
        "11111",
        "Real-time reconstruction of deformable objects",
        "CVLAB",
        "Teacher2",
        "TA2",
        1,
        listOf<String>(),
        false,
        true,
        listOf(tagList[3], tagList[0]),
        false,
        "Description of project2",
        listOf(),
        listOf(sectionList[3])
    )
    private val p3 = ImmutableProject(
        "00000",
        "Implement a fast driver for a 100 Gb/s network card",
        "DSLAB",
        "Teacher5",
        "TA5",
        3,
        listOf<String>(),
        false,
        true,
        listOf(tagList[2]),
        false,
        "Description of project5",
        listOf(),
        listOf(sectionList[2])
    )



    private val onFailureNotExpected: ((Exception) -> Unit) = { e ->
        e.printStackTrace()
        assertNull(e)
        assertTrue(false)
    }


    @Before
    fun setup() {
        projectsDir.mkdirs()

        unreadableDir.mkdirs()
        unreadableDir.setReadable(false)
        unreadableDir.setWritable(false)

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

        val validDir3 = File(projectsDir, "validDirWithInvalidMap")
        validDir3.mkdirs()

        val invalidFile3 = File(validDir3, "project.data")
        val invalidMap: Map<Int, Int> = mapOf(1 to 1, 2 to 2)
        ObjectOutputStream(FileOutputStream(invalidFile3)).use {
            it.writeObject(invalidMap)
        }
    }

    @After
    fun clean() {
        projectsDir.deleteRecursively()
        unreadableDir.deleteRecursively()
    }

    // Because the database dir is not readable/writable, no projets should be stored
    @Test(timeout = 4000)
    fun shouldNotCrashWithInvalidDir() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, unreadableDir)
        Thread.sleep(500)

        val emptyDB = FakeProjectsDatabase(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, unreadableDir)
        var result: List<ImmutableProject>? = null
        db2.getAllProjects({
            result = it
        }, onFailureNotExpected)

        while (result == null);
        assertEquals(emptyList<ImmutableProject>(), result)
    }

    @Test(timeout = 4000)
    fun getAllIdsWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)
        fakeDB.modify(p3)
        Thread.sleep(500)

        val emptyDB = FakeProjectsDatabase(listOf())
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
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeProjectsDatabase(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result: List<ImmutableProject>? = null
        db2.getAllProjects({
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals(listOf(p2, p3).sortedBy { p -> p.id }, result!!.sortedBy { p -> p.id })


        val p2File = File(File(projectsDir, p2.id), "project.data")
        p2File.delete()

        var result2: List<ImmutableProject>? = null
        db2.getAllProjects({
            result2 = it
        }, onFailureNotExpected)
        while (result2 == null);
        assertEquals(listOf(p3), result2)



        val p3File = File(File(projectsDir, p3.id), "project.data")
        p3File.delete()
        p3File.mkdirs()

        var result3: List<ImmutableProject>? = null
        db2.getAllProjects({
            result3 = it
        }, onFailureNotExpected)
        while (result3 == null);
        assertEquals(emptyList<ImmutableProject>(), result3)

        emptyDB.remove(p2)
        emptyDB.remove(p3)
    }

    @Test(timeout = 4000)
    fun getProjectFromIdWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeProjectsDatabase(listOf())
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
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeProjectsDatabase(listOf())
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
        val fakeDB = FakeProjectsDatabase(projectsList)
        OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)
        fakeDB.add(p3)
        Thread.sleep(100)
        fakeDB.remove(p1)
        Thread.sleep(100)

        val emptyDB = FakeProjectsDatabase(listOf())
        val db2 = OfflineProjectDatabase(emptyDB, projectsDir)
        Thread.sleep(500)

        var result: List<ImmutableProject>? = null
        db2.getProjectsFromTags(listOf(tagList[2]), {
            result = it
        }, onFailureNotExpected)
        while (result == null);
        assertEquals(listOf(p3), result)

    }


    @Test(timeout = 4000)
    fun pushIsCorrectlyCalled() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(projectsList)
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
        val fakeDB = FakeProjectsDatabase(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)

        db.deleteProjectWithId(p2.id, {}, onFailureNotExpected)
        Thread.sleep(100)
        assertEquals(listOf(p1), fakeDB.projects)
    }

    @Test(timeout = 4000)
    fun updateVideoWithProjectIsCorrectlyCalled() {
        val projectsList = listOf(p1)
        val fakeDB = FakeProjectsDatabase(projectsList)
        val db = OfflineProjectDatabase(fakeDB, projectsDir)
        Thread.sleep(500)

        val uri_1: String = "https://remote/uri/1"
        val uri_2: String = "https://remote/uri/2"

        db.updateVideoWithProject(p1.id, uri_1, {}, onFailureNotExpected)
        db.updateVideoWithProject(p1.id, uri_2, {}, onFailureNotExpected)
        Thread.sleep(500)
        assertEquals(1, fakeDB.projects.size)
        assertEquals(listOf(uri_1, uri_2).sorted(), fakeDB.projects[0].videoURI.sorted())
    }


    @Test(timeout = 4000)
    fun listenersWorks() {
        val projectsList = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(projectsList)
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