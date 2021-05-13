package com.sdp13epfl2021.projmag.database.interfaces

import com.sdp13epfl2021.projmag.model.Candidature

/**
 * An interface for a Database of candidatures.
 *
 * It provides methods to get candidatures to a particular projects, or to update a particular candidature.
 */
interface CandidatureDatabase {

    /**
     * Get all candidatures to a project
     *
     * @param projectID the ID of the project
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun getListOfCandidatures(
        projectID: ProjectId,
        onSuccess: (List<Candidature>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Push a candidature to a project and edit its state if present
     * Waiting should be used for new candidature.
     *
     * @param projectId the project the user is applying to
     * @param userId id of the user applying
     * @param newState the new state of the candidature
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushCandidature(
        projectId: ProjectId,
        userId: String,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Remove a candidature from a project.
     *
     * @param projectId the projectId of the project of the candidature
     * @param userId ID of the user whose candidature remove
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun removeCandidature(
        projectId: ProjectId,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Add a listener to a particular project candidature.
     *
     * @param projectID the id of the project to register.
     * @param onChange the listener callback function.
     */
    fun addListener(
        projectID: ProjectId,
        onChange: (ProjectId, List<Candidature>) -> Unit
    )
}