package com.sdp13epfl2021.projmag.database

import android.net.Uri
import android.widget.Toast
import androidx.core.net.toFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Result
import com.sdp13epfl2021.projmag.model.Success

/**
 * Helper class to push a project
 */
class ProjectUploader(
    private val projectDB: ProjectsDatabase,
    private val fileDB: FileDatabase
) {

    /**
     * Upload a video to firebase and edit the link of the video in the project corresponding
     * to the given ProjectId
     */
    private fun uploadVideo(
        id: ProjectId,
        videoUri: Uri,
        showMsg: (String) -> Unit,
        finish: () -> Unit
    ) = videoUri.let { tmpUri ->
        fileDB.pushFile(
            tmpUri.toFile(),
            { newUri ->
                projectDB.updateVideoWithProject(
                    id,
                    newUri.toString(),
                    {
                        showMsg("Project pushed with ID : $id")
                        finish()
                    },
                    { showMsg("Can't link video to project") }
                )
            },
            { showMsg("Can't push video") }
        )
    }

    /**
     * Upload a project (if valid) and upload and attach a video to it,
     * if a video is given (Uri not null)
     */
    private fun upload(
        project: ImmutableProject,
        videoUri: Uri?,
        showMsg: (String) -> Unit,
        finish: () -> Unit
    ) {
        projectDB.pushProject(
            project,
            { id ->
                videoUri?.let { uri -> uploadVideo(id, uri, showMsg, finish) }
                    ?: run {
                        showMsg("Project pushed (without video) with ID : $id")
                        finish()
                    }
            },
            { showMsg("Can't push project") }
        )
    }

    /**
     * Check if the given project is valid or not.
     * If not display a message.
     * Otherwise begin the upload process
     */
    fun checkProjectAndThenUpload(
        maybeProject: Result<ImmutableProject>,
        videoUri: Uri?,
        showMsg: (String) -> Unit,
        finish: () -> Unit
    ) =
        when (maybeProject) {
            is Success<*> -> {
                upload(
                    maybeProject.value as ImmutableProject,
                    videoUri,
                    showMsg,
                    finish
                )
            }
            is Failure<*> -> {
                showMsg(maybeProject.reason)
            }
        }

}