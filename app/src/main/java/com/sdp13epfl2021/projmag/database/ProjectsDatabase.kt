package com.sdp13epfl2021.projmag.database

interface ProjectsDatabase {

    fun getProjectFromIds(
        id: List<ProjectId>,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getProjectsFromName(
        name: String,
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getProjectsFromTags(
        projects: List<String>,
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun pushProjectWithId(
        project: Project,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    )
}

typealias ProjectId = String
typealias Project = Any