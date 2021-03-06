package com.sdp13epfl2021.projmag.database.interfaces

import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.model.ImmutableProject

/**
 * Interface for a Database of Projects
 *
 */
interface ProjectDatabase {

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
     * If the `id` is not present, onSuccess is called with null
     *
     * @param id the id of the project to fetch on the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectFromId(
        id: ProjectId,
        onSuccess: (ImmutableProject?) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously get a All `Projects`
     * and pass them to the `onSuccess` consumer. Otherwise pass
     * an exception to the `onFailure` consumer
     *
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getAllProjects(
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously get `Project`s matching the given
     * `name`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     * If the query is empty, onSuccess is called with an empty list
     *
     * @param name The project name to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromName(
        name: String,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously get `Project`s matching the given
     * `tags`, and pass them to the `onSuccess` consumer. Otherwise pass
     * an `Exception` to the `onFailure` consumer.
     * If the query is empty, onSuccess is called with an empty list
     *
     * @param tags The project tags to search for
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectsFromTags(
        tags: List<String>,
        onSuccess: (List<ImmutableProject>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously push a `Project` to the database, an pass its id
     * to the `onSuccess` consumer. Otherwise pass an `Exception` to
     * the `onFailure` consumer. If project.id == "" then an id is
     * generate upon push, otherwise it is pushed with its id.
     *
     * @param project the project to push to the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun pushProject(
        project: ImmutableProject,
        onSuccess: (ProjectId) -> Unit,
        onFailure: (Exception) -> Unit
    )


    /**
     * Delete a Project matching the given id, and then, call `onSuccess`.
     * Otherwise call `onFailure` with an `Exception`
     * If the id is not present `onSuccess` is called
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

    /**
     * Update the video link along a project.
     *
     * @param id the project id
     * @param uri the new video uri
     * @param onSuccess function called on success
     * @param onFailure function called when an exception arise
     */
    fun updateVideoWithProject(
        id: ProjectId,
        uri: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Attaches a listener callback to the projects collection.
     * It will be trigger at every creation, modification and deletion of project with the given project and the type of change.
     *
     * @param changeListener function called when a change occurs.
     */

    fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit)

    /**
     * Remove the 'changeListener' if it is still attached.
     *
     * @param changeListener the listener to remove.
     */
    fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit)
}

typealias ProjectId = String