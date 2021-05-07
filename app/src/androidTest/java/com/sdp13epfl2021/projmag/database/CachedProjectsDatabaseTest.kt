package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.database.fake.FakeProjectsDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger


class CachedProjectsDatabaseTest {
    private val tagManager = TagsBaseManager()
    private val tagList : List<String> = tagManager.tagsListToStringList(tagManager.getAllTags())
    private val sectionList : List<String> = SectionBaseManager.sectionList()
    private val p1 = ImmutableProject(
        "12345",
        "What fraction of Google searches are answered by Wikipedia?",
        "DLAB",
        "authorID13",
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
        "authorID17",
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
        "authorID19",
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


    @Test(timeout = 1000)
    fun getAllProjectWorksWhenTheDBIsUpdated() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        var all = cachedDB.getAllProjects()
        assertEquals(3, all.size)
        assertTrue(all.containsAll(allBeginning))

        fakeDB.remove(p2)
        all = cachedDB.getAllProjects()
        assertEquals(2, all.size)
        assertTrue(all.contains(p1))
        assertFalse(all.contains(p2))
        assertTrue(all.contains(p3))

        fakeDB.remove(p2)
        all = cachedDB.getAllProjects()
        assertEquals(2, all.size)
        assertTrue(all.contains(p1))
        assertFalse(all.contains(p2))
        assertTrue(all.contains(p3))

        all = cachedDB.getAllProjects()
        assertEquals(2, all.size)
        assertTrue(all.contains(p1))
        assertFalse(all.contains(p2))
        assertTrue(all.contains(p3))

        //Adding a project already in the db <=> update the project
        fakeDB.modify(p1)
        all = cachedDB.getAllProjects()
        assertEquals(2, all.size)
        assertTrue(all.contains(p1))
        assertFalse(all.contains(p2))
        assertTrue(all.contains(p3))

        fakeDB.add(p2)
        all = cachedDB.getAllProjects()
        assertEquals(3, all.size)
        assertTrue(all.containsAll(allBeginning))

        all = cachedDB.getAllProjects()
        assertEquals(3, all.size)
        assertTrue(all.containsAll(allBeginning))
    }

    @Test(timeout = 1000)
    fun getProjectFromIdWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: ImmutableProject? = null

        cachedDB.getProjectFromId(p2.id, { project ->
            result = project
        }, { e ->
            assertNull(e)
        })
        while (result == null);
        assertEquals(p2, result)
    }

    @Test(timeout = 1000)
    fun getProjectsFromNameWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<ImmutableProject>? = null

        cachedDB.getProjectsFromName(p2.name, { projects ->
            result = projects
        }, { e ->
            assertNull(e)
        })

        while (result == null);
        assertEquals(listOf(p2), result)
    }

    @Test(timeout = 1000)
    fun getAllIdsWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<ProjectId>? = null

        cachedDB.getAllIds({ projectsIds ->
            result = projectsIds
        }, { e ->
            assertNull(e)
        })

        while (result == null);
        assertEquals(3, result!!.size)
        assertTrue(result!!.contains(p1.id))
        assertTrue(result!!.contains(p2.id))
        assertTrue(result!!.contains(p3.id))
    }

    @Test(timeout = 2000)
    fun getAllProjectsAsyncWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<ImmutableProject>? = null

        cachedDB.getAllProjects({ projects ->
            result = projects
        }, { e ->
            assertNull(e)
        })

        Thread.sleep(200)
        while (result == null);
        assertEquals(3, result!!.size)
        assertTrue(result!!.contains(p1))
        assertTrue(result!!.contains(p2))
        assertTrue(result!!.contains(p3))
    }

    @Test(timeout = 1000)
    fun getProjectsFromTagsWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<ImmutableProject>? = null

        cachedDB.getProjectsFromTags(listOf(tagList[0], tagList[3]), { projects ->
            result = projects
        }, { e ->
            assertNull(e)
        })

        while (result == null);
        assertEquals(2, result!!.size)
        assertTrue(result!!.contains(p1))
        assertTrue(result!!.contains(p2))
    }

    @Test(timeout = 1000)
    fun pushIsCorrectlyCalled() {
        val allBeginning = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: ProjectId? = null

        cachedDB.pushProject(p3, { projectId ->
            result = projectId
        }, { e ->
            assertNull(e)
        })
        while (result == null);
        assertEquals("90000", result)
        val projects = cachedDB.getAllProjects()
        assertEquals(3, projects.size)
    }

    @Test(timeout = 1000)
    fun deleteProjectWithIdIsCorrectlyCalled() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<ImmutableProject>? = null

        cachedDB.deleteProjectWithId(p3.id, {
            result = cachedDB.getAllProjects()
        }, { e ->
            assertNull(e)
        })

        while (result == null);
        assertEquals(2, result!!.size)
        assertFalse(result!!.contains(p3))
    }

    @Test(timeout = 4000)
    fun updateVideoWithProjectIsCorrectlyCalled() {
        val projectsList = listOf(p1)
        val fakeDB = FakeProjectsDatabase(projectsList)
        val db = CachedProjectsDatabase(fakeDB)
        Thread.sleep(500)

        val uri_1: String = "https://remote/uri/1"
        val uri_2: String = "https://remote/uri/2"

        db.updateVideoWithProject(p1.id, uri_1, {}, { assertTrue(false) })
        db.updateVideoWithProject(p1.id, uri_2, {}, { assertTrue(false) })
        Thread.sleep(500)
        TestCase.assertEquals(1, fakeDB.projects.size)
        TestCase.assertEquals(listOf(uri_1, uri_2).sorted(), fakeDB.projects[0].videoURI.sorted())
    }

    @Test
    fun listenersWorks() {
        val allBeginning = listOf(p1, p2)
        val fakeDB = FakeProjectsDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
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

        cachedDB.addProjectsChangeListener(listener)

        fakeDB.modify(p2)
        fakeDB.remove(p2)
        fakeDB.remove(p2)
        fakeDB.add(p3)

        Thread.sleep(100)
        assertEquals(4, count.get())

        cachedDB.removeProjectsChangeListener(listener)
        fakeDB.modify(p1)
        fakeDB.remove(p1)
        fakeDB.remove(p1)
        fakeDB.add(p1)

        Thread.sleep(100)
        assertEquals(4, count.get())
    }

}