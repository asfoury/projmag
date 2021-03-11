package com.sdp13epfl2021.projmag.database

/**
 * Interface for a Database of Projects
 *
 */
interface ProjectsDatabase {

    /**
     * Asynchronously get all `ProjectId`s from the database
     * and pass them to the `onSuccess` consumer. Otherwise pass
     * an exception to the `onFailure` consumer
     *
     * @param onSuccess
     * @param onFailure
     */
    fun getAllIds(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously get a `Projects` from its `id`
     * and pass it to the `onSuccess` consumer. Otherwise pass
     * an exception to the `onFailure` consumer
     *
     * @param ids the id of the project to fetch on the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectFromId(
        id: ProjectId,
        onSuccess: (Project) -> Unit,
        onFailure: (Exception) -> Unit
    )

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
     * Asynchronoulsy get `Project`s matching the given
     * `name`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     *
     * @param name The project name to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromName(
        name: String,
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronoulsy get `Project`s matching the given
     * `tags`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     *
     * @param tags The project tags to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<Project>) -> Unit,
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


    /**
     * Delete a Project matching the given id, and then, call `onSuccess`.
     * Otherwise call `onFailure` with an `Exception`
     *
     * @param id the project id
     * @param onSuccess function called on success
     * @param onFailure function called when an exception arise
     */
    fun deleteProjectWithId(
        id: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}

typealias ProjectId = String
typealias Project = DummyProject?