package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.Candidature

/**
 * An interface for a Database of candidatures to project
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
     * @param candidature the candidature of this user
     * @param newState the new state of the candidature
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushCandidature(
        candidature: Candidature,
        newState: Candidature.State,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

}