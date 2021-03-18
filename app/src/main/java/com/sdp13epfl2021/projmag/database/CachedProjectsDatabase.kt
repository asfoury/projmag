package com.sdp13epfl2021.projmag.database

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CachedProjectsDatabase(private val db: ProjectsDatabase) : ProjectsDatabase {

    private val TAG = "CachedProjectDatabase"
    private var projects: List<Project> = emptyList()
    private var listeners: List<((ProjectChange) -> Unit)> = emptyList()

    init {
        db.addProjectsChangeListener { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> addProject(change.project)
                ProjectChange.Type.MODIFIED -> addProject(change.project)
                ProjectChange.Type.REMOVED -> change.project?.let {
                    it.id?.run {
                        removeProjectWithId(
                            this
                        )
                    }
                }
            }
            listeners.forEach { it -> it(change) }
        }
        db.getAllProjects({ all -> all.forEach(this::addProject) }, { e ->
            Log.d(TAG, "failed to load projects from database.", e)
        })
    }


    /**
     * Add a Project to the local cache.
     * If the cache contains a project with the same id,
     * the local project is removed and replaced by the new one.
     * If the project is null, nothing is done.
     */
    @Synchronized
    private fun addProject(project: Project) {
        project?.let {
            it.id?.run { removeProjectWithId(this) }
            projects = projects + it
        }

    }

    /**
     * Remove a project from its id, from the local cache.
     * If the project is not present, nothing is done.
     */
    @Synchronized
    private fun removeProjectWithId(id: String) {
        projects = projects.filter { p -> p!!.id != id }
    }


    /**
     * Synchronously get all projects cached locally.
     * This list is guaranteed to have only non null Project.
     */
    fun getAllProjects(): List<Project> {
        return projects
    }


    override fun getAllIds(onSuccess: (List<String>) -> Unit, onFailure: (Exception) -> Unit) {
        GlobalScope.launch { onSuccess(projects.mapNotNull { p -> p!!.id }) }
    }

    override fun getProjectFromId(
        id: String,
        onSuccess: (Project) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects.find { p -> p!!.id == id }) }
    }

    override fun getAllProjects(
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects) }
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects.filter { p -> p!!.name == name }) }
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch {
            val listOfTags = tags.map { tag -> tag.toLowerCase(Locale.ROOT) }
            onSuccess(projects.filter { p ->
                p!!.tags.any { tag ->
                    listOfTags.contains(
                        tag.toLowerCase(
                            Locale.ROOT
                        )
                    )
                }
            }
            )
        }
    }

    override fun pushProject(
        project: Project,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.pushProject(project, onSuccess, onFailure)
    }

    override fun deleteProjectWithId(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.deleteProjectWithId(id, onSuccess, onFailure)
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