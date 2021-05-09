package com.sdp13epfl2021.projmag.database.impl.cache

import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.ImmutableProject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * An implementation of ProjectDatabase that keep a cached list of projects and rely on another ProjectDatabase.
 *
 * Any call to a getXXX methods will be performed on a cached list, that is not always up to date, but listeners can be used in theses cases.
 *
 * Any call to  push/delete/update methods will be forward to the given db.
 *
 * @param db a ProjectDatabase that will be used as a base database.
 */
class CachedProjectDatabase(private val db: ProjectDatabase) : ProjectDatabase {

    private var projects: List<ImmutableProject> = emptyList()
    private var listeners: List<((ProjectChange) -> Unit)> = emptyList()

    /**
     * Load projects from the given db and register for any change.
     *
     * Only one local ChangeListener is add to the given db.
     * All other listeners are called from this one, when a change occurs.
     */
    init {
        db.addProjectsChangeListener { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> addProject(change.project)
                ProjectChange.Type.MODIFIED -> addProject(change.project)
                ProjectChange.Type.REMOVED -> removeProjectWithId(change.project.id)
            }
            listeners.forEach { it -> it(change) }
        }
        db.getAllProjects({ all ->
            all.forEach { p ->
                addProject(p)
                val change = ProjectChange(ProjectChange.Type.ADDED, p)
                listeners.forEach { it -> it(change) }
            }
        }, {})
    }


    /**
     * Add a Project to the local cache.
     *
     * If the cache contains a project with the same id,
     * the local project is removed and replaced by the new one.
     */
    @Synchronized
    private fun addProject(project: ImmutableProject) {
        removeProjectWithId(project.id)
        projects = projects + project

    }

    /**
     * Remove a project from its id, from the local cache.
     *
     * If the project is not present, nothing is done.
     */
    @Synchronized
    private fun removeProjectWithId(id: ProjectId) {
        projects = projects.filter { p -> p.id != id }
    }


    /**
     * Synchronously get all projects cached locally.
     *
     * This list is guaranteed to have only non null Project.
     */
    fun getAllProjects(): List<ImmutableProject> {
        return projects
    }


    override fun getAllIds(onSuccess: (List<ProjectId>) -> Unit, onFailure: (Exception) -> Unit) {
        GlobalScope.launch { onSuccess(projects.map { p -> p.id }) }
    }

    override fun getProjectFromId(
        id: ProjectId,
        onSuccess: (ImmutableProject?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects.find { p -> p.id == id }) }
    }

    override fun getAllProjects(
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects) }
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch { onSuccess(projects.filter { p -> p.name == name }) }
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        GlobalScope.launch {
            val listOfTags = tags.map { tag -> tag.toLowerCase(Locale.ROOT) }
            onSuccess(projects.filter { p ->
                p.tags.any { tag ->
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