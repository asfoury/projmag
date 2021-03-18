package com.sdp13epfl2021.projmag.database

import java.lang.Exception

/**
 * An interface to manage a database containing
 * user data
 *
 */
interface UserDataDatabase {
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
        projectID: String,
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
        projectIDs: List<String>,
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
        onSuccess: (List<String>) -> Unit,
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
        projectID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}