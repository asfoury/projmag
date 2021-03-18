package com.sdp13epfl2021.projmag.database

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger


class CachedProjectsDatabaseTest {
    private val p1 = Project("12345","What fraction of Google searches are answered by Wikipedia?","DLAB","Robert West","TA1",1, listOf<String>(),false,true, listOf("data analysis","large datasets","database","systems","database","systems"),false,"Description of project1")
    private val p2 = Project("11111","Real-time reconstruction of deformable objects","CVLAB","Teacher2","TA2",1, listOf<String>(),false,true, listOf("Computer Vision","ML"),false,"Description of project2")
    private val p3 = Project("00000","Implement a fast driver for a 100 Gb/s network card","DSLAB","Teacher5","TA5",3, listOf<String>(),false,true, listOf("Low Level","Networking","Driver"),false,"Description of project5")


    @Test(timeout = 1000)
    fun getAllProjectWorksWhenTheDBIsUpdated() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
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

        fakeDB.remove(null)
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

        fakeDB.add(null)
        all = cachedDB.getAllProjects()
        assertEquals(3, all.size)
        assertTrue(all.containsAll(allBeginning))
    }

    @Test(timeout = 1000)
    fun getProjectFromIdWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: Project = null

        p2.id?.let {
            cachedDB.getProjectFromId(it, { project ->
                result = project
            }, { e ->
                assertNull(e)
            })
        }
        while (result == null);
        assertEquals(p2, result)
    }

    @Test(timeout = 1000)
    fun getProjectsFromNameWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<Project>? = null

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
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<String>? = null

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
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<Project>? = null

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
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<Project>? = null

        cachedDB.getProjectsFromTags(listOf("ml", "Driver"), { projects ->
            result = projects
        }, { e ->
            assertNull(e)
        })

        while (result == null);
        assertEquals(2, result!!.size)
        assertTrue(result!!.contains(p2))
        assertTrue(result!!.contains(p3))
    }

    @Test(timeout = 1000)
    fun pushIsCorrectlyCalled() {
        val allBeginning = listOf(p1, p2)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: String? = null

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
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)
        var result: List<Project>? = null

        p3.id?.let {
            cachedDB.deleteProjectWithId(it, {
                result = cachedDB.getAllProjects()
            }, { e ->
                assertNull(e)
            })
        }

        while (result == null);
        assertEquals(2, result!!.size)
        assertFalse(result!!.contains(p3))
    }

    @Test
    fun listenersWorks() {
        val allBeginning = listOf(p1, p2)
        val fakeDB = FakeDatabase(allBeginning)
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




    private class FakeDatabase(projectsBeginning: List<Project>) : ProjectsDatabase {
        private var projects: List<Project> = projectsBeginning
        private var listeners: List<((ProjectChange) -> Unit)> = emptyList()
        private var nextId: Int = 90000

        fun add(project: Project) {
            projects = projects + project
            listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.ADDED, project)) }
        }

        fun modify(project: Project) {
            projects.filter { p -> p?.id != project?.id }
            projects = projects + project
            listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.MODIFIED, project)) }
        }

        fun remove(project: Project) {
            projects = projects + project
            listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.REMOVED, project)) }
        }

        override fun getAllIds(
            onSuccess: (List<String>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun getProjectFromId(
            id: String,
            onSuccess: (Project) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun getAllProjects(
            onSuccess: (List<Project>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            onSuccess(projects)
        }

        override fun getProjectsFromName(
            name: String,
            onSuccess: (List<Project>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun getProjectsFromTags(
            tags: List<String>,
            onSuccess: (List<Project>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun pushProject(
            project: Project,
            onSuccess: (String) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            val pid = nextId.toString()
            nextId += 1
            val newProject = project?.let {Project(pid,
                it.name, it.lab, it.teacher, it.TA, it.nbParticipant, it.assigned, it.masterProject,
                it.bachelorProject, it.tags, it.isTaken, it.description)
            }
            add(newProject)
            onSuccess(pid)
        }

        override fun deleteProjectWithId(
            id: String,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            remove(projects.find {p -> p?.id == id})
            onSuccess()
        }

        override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
            listeners = listeners + changeListener
        }

        override fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
            listeners = listeners - changeListener
        }

    }
}