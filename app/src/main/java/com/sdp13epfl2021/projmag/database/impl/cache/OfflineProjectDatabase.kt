package com.sdp13epfl2021.projmag.database.impl.cache

import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.saveToFile
import com.sdp13epfl2021.projmag.model.ImmutableProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.util.*


/**
 * An implementation of ProjectDatabase that store/load projects from local storage and rely on another ProjectsDatabase.
 *
 * Any call to a getXXX methods will use a combined list of local storage and db (priority to db -> up to date).
 *
 * Any call to  push/delete/update methods will be forward to the given db.
 *
 * @param db a ProjectDatabase that will be used as a base database.
 * @param projectsDir the directory of projects, any directory inside represent a project.
 */
class OfflineProjectDatabase(
    private val db: ProjectDatabase,
    private val projectsDir: File,
    candidatureDB: CandidatureDatabase
) : ProjectDatabase {

    private val projectDataFilename: String = "project.data"
    private val idRegex: Regex = Regex("^[a-zA-Z0-9]*\$")

    /**
     * Projects are not stored in memory, we only keep track of projectId for valid projects.
     */
    private var projectsFiles: Map<ProjectId, File> = emptyMap()
    private var listeners: List<((ProjectChange) -> Unit)> = emptyList()

    /**
     * Load projects from the local storage then from the given db and register for any change.
     *
     * Only one local ChangeListener is add to the given db.
     * All other listeners are called from this one, when a change occurs.
     */
    init {
        projectsDir.mkdirs()
        loadProjects()

        db.addProjectsChangeListener { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> saveProject(change.project)
                ProjectChange.Type.MODIFIED -> saveProject(change.project)
                ProjectChange.Type.REMOVED -> deleteProject(change.project.id)
            }
            listeners.forEach { it -> it(change) }
        }

        db.getAllProjects({ remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                remoteProjects.forEach { p ->
                    saveProject(p)
                    candidatureDB.addListener(p.id) { _, _ -> }
                }
            }
        }, {})
    }

    /**
     * This method is called once when initialized.
     *
     * Loads all valid projects found in the local storage from the files /projects/{projectId}/project.data.
     */
    private fun loadProjects() {
        projectsFiles = projectsDir
            .listFiles()
            ?.let {
                it.map { child -> child.name to File(child, projectDataFilename) }
                    .filter { (id, data) ->
                        id.matches(idRegex) && data.exists() && data.isFile && readProject(
                            data
                        ) != null
                    }
                    .toMap()
            } ?: emptyMap()
    }

    @Synchronized
    private fun deleteProject(projectId: ProjectId) {
        val projectFile = projectsFiles[projectId]
        projectsFiles = projectsFiles - projectId
        if (projectFile != null && projectFile.exists()) {
            synchronized(projectFile) {
                projectFile.parentFile?.deleteRecursively()
            }
        }
    }

    @Synchronized
    private fun saveProject(project: ImmutableProject) {
        val projectDir = File(projectsDir, project.id)
        if (projectDir.isDirectory || projectDir.mkdir()) {
            var projectFile = projectsFiles[project.id]
            if (projectFile == null) {
                projectFile = File(projectDir, projectDataFilename)
                projectsFiles = projectsFiles + (project.id to projectFile)
            }
            saveToFile(projectFile, project.toMapString())
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun readProject(file: File): ImmutableProject? {
        synchronized(file) {
            if (file.exists() && file.isFile) {
                try {
                    val id: ProjectId = file.parentFile!!.name
                    ObjectInputStream(FileInputStream(file)).use {
                        val map: HashMap<String, Any> = it.readObject() as HashMap<String, Any>
                        val project = ImmutableProject.buildFromMap(map, id)
                        if (project == null) {
                            deleteProject(id)
                        }
                        return project
                    }
                } catch (e: Exception) {
                    file.delete()
                    return null
                }
            } else {
                return null
            }
        }
    }


    override fun getAllIds(onSuccess: (List<ProjectId>) -> Unit, onFailure: (Exception) -> Unit) {
        db.getAllIds({ remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                val localProjects = projectsFiles
                    .filterKeys { id -> !remoteProjects.contains(id) }
                    .map { entry -> entry.key }
                onSuccess(remoteProjects + localProjects)
            }
        }, onFailure)
    }

    override fun getProjectFromId(
        id: ProjectId,
        onSuccess: (ImmutableProject?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val projectFile: File? = projectsFiles[id]
        if (projectFile == null) {
            db.getProjectFromId(id, onSuccess, onFailure)
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                onSuccess(readProject(projectFile))
            }
        }
    }

    override fun getAllProjects(
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.getAllProjects({ remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                val localProjects = projectsFiles
                    .filterKeys { id -> !remoteProjects.any { p -> p.id == id } }
                    .mapNotNull { entry -> readProject(entry.value) }
                onSuccess(remoteProjects + localProjects)
            }
        }, onFailure)
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.getProjectsFromName(name, { remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                val localProjects = projectsFiles
                    .filterKeys { id -> !remoteProjects.any { p -> p.id == id } }
                    .mapNotNull { entry -> readProject(entry.value) }
                    .filter { p -> p.name == name }
                onSuccess(remoteProjects + localProjects)
            }
        }, onFailure)
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.getProjectsFromTags(tags, { remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                val listOfTags = tags.map { tag -> tag.toLowerCase(Locale.ROOT) }
                val localProjects = projectsFiles
                    .filterKeys { id -> !remoteProjects.any { p -> p.id == id } }
                    .mapNotNull { entry -> readProject(entry.value) }
                    .filter { p -> p.tags.any { tag -> listOfTags.contains(tag.toLowerCase(Locale.ROOT)) } }
                onSuccess(remoteProjects + localProjects)
            }
        }, onFailure)
    }

    override fun pushProject(
        project: ImmutableProject,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.pushProject(project, onSuccess, onFailure)
    }

    override fun deleteProjectWithId(
        id: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.deleteProjectWithId(id, onSuccess, onFailure)
    }

    override fun updateVideoWithProject(
        id: ProjectId,
        uri: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.updateVideoWithProject(id, uri, onSuccess, onFailure)
    }

    @Synchronized
    override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        listeners = listeners + changeListener
    }

    @Synchronized
    override fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        listeners = listeners - changeListener
    }

}