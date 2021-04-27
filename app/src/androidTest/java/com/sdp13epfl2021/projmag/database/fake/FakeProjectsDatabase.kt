package com.sdp13epfl2021.projmag.database.fake

import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.database.ProjectsDatabase
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Success
import java.util.*
import kotlin.NoSuchElementException


class FakeProjectsDatabase(projectsBeginning: List<ImmutableProject> = emptyList()) :
    ProjectsDatabase {

    var projects: List<ImmutableProject> = projectsBeginning
    var listeners: List<((ProjectChange) -> Unit)> = emptyList()
    var nextId: Int = 90000

    fun add(project: ImmutableProject) {
        projects = projects + project
        listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.ADDED, project)) }
    }

    fun modify(project: ImmutableProject) {
        projects = projects.filter { p -> p.id != project.id }
        projects = projects + project
        listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.MODIFIED, project)) }
    }

    fun remove(project: ImmutableProject) {
        projects = projects - project
        listeners.forEach{ it -> it(ProjectChange(ProjectChange.Type.REMOVED, project)) }
    }

    override fun getAllIds(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(projects.map { p -> p.id })
    }

    override fun getProjectFromId(
        id: ProjectId,
        onSuccess: (ImmutableProject?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(projects.find { p -> p.id == id })
    }

    override fun getAllProjects(
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(projects)
    }

    override fun getProjectsFromName(
        name: String,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        onSuccess(projects.filter { p -> p.name == name })
    }

    override fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val tagsList = tags.map { tag -> tag.toLowerCase(Locale.ROOT)}
        onSuccess(projects.filter { tagsList.any {tag -> tagsList.contains(tag.toLowerCase(Locale.ROOT))} })
    }

    override fun pushProject(
        project: ImmutableProject,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val pid = nextId.toString()
        nextId += 1
        val newProject = project.let {
            ImmutableProject(
                pid,
                it.name, it.lab, it.teacher, it.TA, it.nbParticipant, it.assigned, it.masterProject,
                it.bachelorProject, it.tags, it.isTaken, it.description,
            )
        }
        add(newProject)
        onSuccess(pid)
    }

    override fun deleteProjectWithId(
        id: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        projects.find { p -> p.id == id }?.let { remove(it) }
        onSuccess()
    }

    override fun updateVideoWithProject(
        id: ProjectId,
        uri: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val oldProject = projects.find { p -> p.id == id }
        if (oldProject != null) {
            projects = projects - oldProject
            val newProject = oldProject.build_copy( videoURI = oldProject.videoURI + uri)
            when (newProject) {
                is Success -> {
                    projects = projects + newProject.value
                    onSuccess()
                }
                is Failure -> onFailure(Exception(newProject.reason))
            }
        } else {
            onFailure(NoSuchElementException("No project with id : $id"))
        }
    }

    override fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        listeners = listeners + changeListener
    }

    override fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit) {
        listeners = listeners - changeListener
    }

}

