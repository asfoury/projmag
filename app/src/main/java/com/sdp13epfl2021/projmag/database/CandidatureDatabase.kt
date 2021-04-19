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
     * Push a candidature to a project
     *
     * @param projectID the ID of the project
     * @param candidature the candidature of this user
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushCandidature(
        projectID: ProjectId,
        candidature: Candidature,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Accept a candidature to a project
     *
     * @param projectID the ID of the project
     * @param userID the ID of the student accepted
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun acceptCandidature(
        projectID: ProjectId,
        userID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Reject a candidature to a project
     *
     * @param projectID the ID of the project
     * @param userID the ID of the student rejected
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun rejectCandidature(
        projectID: ProjectId,
        userID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

}