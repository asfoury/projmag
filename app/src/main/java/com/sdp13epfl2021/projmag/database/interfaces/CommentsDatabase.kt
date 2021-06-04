package com.sdp13epfl2021.projmag.database.interfaces

import com.sdp13epfl2021.projmag.model.Message

interface CommentsDatabase {
    /**
     * Adds a comment message to the comments of a project
     * Call `onSuccess` if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param message The message to be added
     * @param projectId The ID of the project
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun addCommentToProjectComments(
        message: Message,
        projectId: ProjectId,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Gets the comments of a project
     * Call `onSuccess` with the a `List` of messages if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param projectId The ID of the project
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun getCommentsOfProject(
        projectId: ProjectId,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Adds a listener to the comments of a project
     * @param projectID The ID of the project
     * @param onChange called on success
     */
    fun addListener(
        projectID: ProjectId,
        onChange: (ProjectId, List<Message>) -> Unit
    )


}