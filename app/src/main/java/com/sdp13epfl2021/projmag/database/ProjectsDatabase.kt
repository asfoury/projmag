package com.sdp13epfl2021.projmag.database

interface ProjectsDatabase {

    /**
     * Asynchronously get a `List` of `Projects` from their `id`
     * and pass them to the `onSuccess` consumer. Otherwise pass
     * an exception to the `onFailure` consumer
     *
     * @param ids the id's of the projects to fetch on the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectFromIds(
        ids: List<ProjectId>,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronoulsy get `ProjectId`'s of `Project`s matching the given
     * `name`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     *
     * @param name The project name to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromName(
        name: String,
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronoulsy get `ProjectId`'s of `Project`s matching the given
     * `tags`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     *
     * @param tags The project tags to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously push a `Project` to the database, an pass its id
     * to the `onSuccess` consumer. Otherwise pass an `Exception` to
     * the `onFailure` consumer.
     *
     * @param project the project to push to the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun pushProjectWithId(
        project: Project,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    )
}

typealias ProjectId = String
typealias Project = Any