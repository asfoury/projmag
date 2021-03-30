package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import kotlin.collections.HashMap

private const val PROJECT_DATA_FILE: String = "project.data"
private val ID_PATTERN: Regex = Regex("^[a-zA-Z0-9]*\$")

class OfflineProjectDatabase(private val db: ProjectsDatabase, private val projectsDir: File) : ProjectsDatabase {

    private var projectsFiles: Map<ProjectId, File> = emptyMap()

    init {
        projectsDir.mkdirs()
        loadProjects()

        db.addProjectsChangeListener { change ->
            when(change.type) {
                ProjectChange.Type.ADDED -> saveProject(change.project)
                ProjectChange.Type.MODIFIED -> saveProject(change.project)
                ProjectChange.Type.REMOVED -> deleteProject(change.project.id)
            }
        }

        db.getAllProjects({ remoteProjects ->
            GlobalScope.launch(Dispatchers.IO) {
                remoteProjects.forEach { p -> saveProject(p) }
                loadProjects()
            }
        }, {})
    }

    @Synchronized
    private fun loadProjects() {
        projectsFiles = projectsDir
            .listFiles()!!
            .map { child -> child.name to File(child, PROJECT_DATA_FILE) }
            .filter { (id, data) -> id.matches(ID_PATTERN) && data.exists() && data.isFile }
            .toMap()
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
                projectFile = File(projectDir, PROJECT_DATA_FILE)
                projectsFiles = projectsFiles + (project.id to projectFile)
            }
            writeProject(project, projectFile)
        }
    }

    private fun writeProject(project: ImmutableProject, file: File) {
        ObjectOutputStream(FileOutputStream(file, false)).use {
            it.writeObject(project.toMapString())
        }
    }

    private fun readProject(file: File): ImmutableProject? {
        synchronized(file) {
            if (file.exists() && file.isFile) {
                ObjectInputStream(FileInputStream(file)).use {
                    val map: HashMap<String, Any> = it.readObject() as HashMap<String, Any>
                    val result = ImmutableProject.build(
                        id = file.parentFile.name,
                        name = map["name"] as String,
                        lab = map["lab"] as String,
                        teacher = map["teacher"] as String,
                        TA = map["TA"] as String,
                        nbParticipant = map["nbParticipant"] as Int,
                        assigned = (map["assigned"] as? List<String>) ?: listOf(),
                        masterProject = map["masterProject"] as Boolean,
                        bachelorProject = map["bachelorProject"] as Boolean,
                        tags = (map["tags"] as? List<String>) ?: listOf(),
                        isTaken = map["isTaken"] as Boolean,
                        description = map["description"] as String
                    )
                    return when (result) {
                        is Success -> result.value
                        is Failure -> null
                    }
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
                    .filter { p -> p.name == name}
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

    override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        db.addProjectsChangeListener(changeListener)
    }

    override fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        db.removeProjectsChangeListener(changeListener)
    }

}