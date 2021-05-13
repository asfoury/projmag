package com.sdp13epfl2021.projmag.database.interfaces

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import com.sdp13epfl2021.projmag.model.ProjectFilter

/**
 * An interface to manage a database containing user data, such as favorites, cv, applications, preferences.
 */
interface UserdataDatabase {
    /**
     * Push the ID of a favorite `Project` to the database
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param projectID The ID of the favorite project
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushFavoriteProject(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Push a `List` of IDs of favorite `Project`s to the database
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param projectIDs The list of IDs of the favorite project
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushListOfFavoriteProjects(
        projectIDs: List<ProjectId>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Get a `List` of IDs of favorite `Project`s to the database
     * Call `onSuccess` with the a `List` of IDs if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun getListOfFavoriteProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Remove a `Project` from the favorites
     * Call on `onSuccess` in case of success
     * Call on `onFailure` with an `Exception` in case of failure
     *
     * @param projectID the ID of project to remove from favorites
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun removeFromFavorite(
        projectID: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Push the given cv to the database
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param cv the cv to push to the database
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun pushCv(
        cv: CurriculumVitae,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Get the cv for the given user.
     * If no cv was found for this user, onSuccess is called with null.
     *
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param userID the user id of the cv to get
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun getCv(
        userID: String,
        onSuccess: (CurriculumVitae?) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Apply or unapply to the given project depending on the value of apply
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param apply function applies if true, else unapplies
     * @param projectId the project to apply or unapply to
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun applyUnapply(
        apply: Boolean,
        projectId: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Gets a `List` of IDs of applied to `Project`s to the database
     * Call `onSuccess` with the a `List` of IDs if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun getListOfAppliedToProjects(
        onSuccess: (List<ProjectId>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Get the user preferences from Firebase
     * If none are set, return `null`.
     *
     * @param onSuccess Called on success with the fetched or null
     * @param onFailure Called no failure with an `Exception`
     */
    fun getPreferences(
        onSuccess: (ProjectFilter?) -> Unit,
        onFailure: (Exception) -> Unit
    )


    /**
     * Push the given project to Firebase. It will be linked with the user who pushed it.
     *
     * @param pf The given `ProjectFilter`
     * @param onSuccess Called on success
     * @param onFailure Called on Failure with an `Exception`
     */
    fun pushPreferences(
        pf: ProjectFilter,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Uploads the user profile to a database
     * @param profile the user profile to upload
     * @param onSuccess the closure that's when a profile is uploaded successfully
     * @param onFailure the closure that's called if the upload fails
     */
    fun uploadProfile(
        profile: ImmutableProfile,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Gets the user profile from the database if it exists, null otherwise.
     *
     * @param userID the id of the user
     * @param onSuccess the closure that's when a profile is downloaded successfully with the fetched profile passed to it
     * @param onFailure the closure that's called if the download fails
     */
    fun getProfile(
        userID: String,
        onSuccess: (ImmutableProfile?) -> Unit,
        onFailure: (Exception) -> Unit
    )
}
