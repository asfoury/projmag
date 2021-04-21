package com.sdp13epfl2021.projmag.database

import android.net.Uri
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ImmutableProjectResult
import com.sdp13epfl2021.projmag.model.Success

/**
 * Helper class to push a project
 *
 * @param projectDB Database of project
 * @param fileDB Database of files
 * @param showMsg a callback consumer with takes string message
 * @param onFailure a method which is always called at the end of a filed process
 * @param onSuccess a method which is always called at the end of a successful process
 */
class ProjectUploader(
    private val projectDB: ProjectsDatabase,
    private val fileDB: FileDatabase,
    private val metadataDB: MetadataDatabase,
    private val showMsg: (String) -> Unit,
    private val onFailure: () -> Unit,
    private val onSuccess: () -> Unit
) {
    /**
     * Upload a video to firebase and edit the link of the video in the project corresponding
     * to the given ProjectId
     */
    private fun uploadVideo(
        id: ProjectId,
        videoUri: Uri,
        subtitles: String?
    ) = fileDB.pushFileFromUri(
        videoUri,
        { newUri ->
            projectDB.updateVideoWithProject(
                id,
                newUri.toString(),
                {
                    showMsg("Project pushed with ID : $id")
                    onSuccess()
                },
                {
                    showMsg("Project pushed with ID : $id. (Video can't be linked)")
                    fileDB.deleteFile(newUri.toString(), {}, {})
                    onSuccess() // call onSuccess because the project has been pushed
                }
            )
            subtitles?.let { sub ->
                metadataDB.addSubtitlesToVideo(
                    newUri.toString(),
                    "en",
                    sub,
                    {},
                    {}
                )
            }
        },
        {
            showMsg("Can't push video")
            onFailure()
        }
    )


    /**
     * Upload a project (if valid) and upload and attach a video to it,
     * if a video is given (Uri not null)
     */
    private fun upload(
        project: ImmutableProject,
        videoUri: Uri?,
        subtitles: String?
    ) {
        projectDB.pushProject(
            project,
            { id ->
                videoUri?.let { uri ->
                    uploadVideo(id, uri, subtitles)
                }
                    ?: run {
                        showMsg("Project pushed (without video) with ID : $id")
                        onSuccess()
                    }
            },
            {
                showMsg("Can't push project")
                onFailure()
            }
        )
    }

    /**
     * Check if the given project is valid or not.
     * If not display a message.
     * Otherwise begin the upload process
     */
    fun checkProjectAndThenUpload(
        maybeProject: ImmutableProjectResult<ImmutableProject>,
        videoUri: Uri?,
        subtitles: String?
    ) =
        when (maybeProject) {
            is Success<*> -> {
                upload(
                    maybeProject.value as ImmutableProject,
                    videoUri,
                    subtitles
                )
            }
            is Failure<*> -> {
                showMsg(maybeProject.reason)
                onFailure()
            }
        }

}