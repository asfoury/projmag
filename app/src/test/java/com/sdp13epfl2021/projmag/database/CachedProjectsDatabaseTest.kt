package com.sdp13epfl2021.projmag.database

import org.junit.Assert.*
import org.junit.Test


class CachedProjectsDatabaseTest {
    private val p1 = Project("12345","What fraction of Google searches are answered by Wikipedia?","DLAB","Robert West","TA1",1, listOf<String>(),false,true, listOf("data analysis","large datasets","database","systems","database","systems"),false,"Description of project1")
    private val p2 = Project("11111","Real-time reconstruction of deformable objects","CVLAB","Teacher2","TA2",1, listOf<String>(),false,true, listOf("Computer Vision","ML"),false,"Description of project2")
    private val p3 = Project("00000","Implement a fast driver for a 100 Gb/s network card","DSLAB","Teacher5","TA5",3, listOf<String>(),false,true, listOf("Low Level","Networking","Driver"),false,"Description of project5")


    @Test
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
    }

    @Test
    fun getProjectFromIdWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.getProjectFromId(p2.id, { project ->
            assertEquals(p2, project)
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun getProjectsFromNameWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.getProjectsFromName(p2.name, { projects ->
            assertEquals(listOf(p2), projects)
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun getAllIdsWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.getAllIds({ projectsIds ->
            assertEquals(3, projectsIds.size)
            assertTrue(projectsIds.contains(p1.id))
            assertTrue(projectsIds.contains(p2.id))
            assertTrue(projectsIds.contains(p3.id))
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun getAllProjectsAsyncWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.getAllProjects({ projects ->
            assertEquals(3, projects.size)
            assertTrue(projects.contains(p1))
            assertTrue(projects.contains(p2))
            assertTrue(projects.contains(p3))
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun getProjectsFromTagsWorks() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.getProjectsFromTags(listOf("ml", "Driver"), { projects ->
            assertEquals(2, projects.size)
            assertTrue(projects.contains(p2))
            assertTrue(projects.contains(p3))
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun pushIsCorrectlyCalled() {
        val allBeginning = listOf(p1, p2)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.pushProject(p3, { projectId ->
            val projects = cachedDB.getAllProjects()
            assertEquals("90000", projectId)
            assertEquals(3, projects.size)
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
    }

    @Test
    fun deleteProjectWithIdIsCorrectlyCalled() {
        val allBeginning = listOf(p1, p2, p3)
        val fakeDB = FakeDatabase(allBeginning)
        val cachedDB = CachedProjectsDatabase(fakeDB)

        cachedDB.deleteProjectWithId(p3.id, {
            val projects = cachedDB.getAllProjects()
            assertEquals(2, projects.size)
            assertFalse(projects.contains(p3))
            //assertTrue(false)
        }, { e ->
            assertNull(e)
        })
        Thread.sleep(1000)
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
            onSuccess: (List<ProjectId>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun getProjectFromId(
            id: ProjectId,
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
            onSuccess: (ProjectId) -> Unit,
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
            id: ProjectId,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            remove(projects.find {p -> p?.id == id})
            onSuccess()
        }

        override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
            listeners = listeners + changeListener
        }

    }
}