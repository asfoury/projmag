package com.sdp13epfl2021.projmag.database

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

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
     * If the `id` is not present, onSuccess is called with null
     *
     * @param id the id of the project to fetch on the database
     * @param onSuccess the consumer for successful results
     * @param onFailure the consumer for failures
     */
    fun getProjectFromId(
        id: ProjectId,
        onSuccess: (Project) -> Unit,
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
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronoulsy get `Project`s matching the given
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
        onSuccess: (List<Project>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronoulsy get `Project`s matching the given
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
    fun pushProject(
        project: Project,
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
     * Attaches the 'changeListener' to the projects collection.
     * It will be trigger at every creation, modification and deletion of project.
     */
    fun addProjectsChangeListener(changeListener: (ProjectChange) -> Unit)

    /**
     * Remove the 'changeListener' if it is still attached.
     */
    fun removeProjectsChangeListener(changeListener: (ProjectChange) -> Unit)
}

typealias ProjectId = String
typealias Project = DummyProject?