package com.sdp13epfl2021.projmag.database.interfaces

import android.net.Uri
import java.io.File

/**
 * An interface for a database of files.
 * It provides methods for get/push/delete files.
 */
interface FileDatabase {

    /**
     * Asynchronously get the file from the database
     * The file will be downloaded inside `destinationFolder`
     * If there already is a file with the same name, nothing is downloaded.
     *
     * Call `onSuccess` with the local file if the operation succeeded or if file already present.
     * Call `onFailure` with an Exception in case of failure
     *
     * @param fileUrl the url of the file
     * @param destinationFolder the folder where to save the file
     * @param onSuccess the consumer for success
     * @param onFailure the consumer for failures
     */
    fun getFile(
        fileUrl: String,
        destinationFolder: File,
        onSuccess: (File) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously push the file to the database
     * The name stored is a random string appended by the original name
     * If the same file is push multiple times, it will result to different files
     *
     * Call `onSuccess` with the Uri if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param file the File to push
     * @param onSuccess the consumer for success
     * @param onFailure the consumer for failures
     */
    fun pushFile(
        file: File,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously push the file from uri to the database
     * If the file already exist, it will overwrite it
     * Call `onSuccess` with the Uri if the operation succeeded
     * Call `onFailure` with an Exception in case of failure
     *
     * @param uri the File to push
     * @param onSuccess the consumer for success
     * @param onFailure the consumer for failures
     */
    fun pushFileFromUri(
        uri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    )

    /**
     * Asynchronously delete a file from the database
     *
     * Call `onSuccess` if the operation succeeded or if the file doesn't exist
     * Call `onFailure` with an Exception in case of failure
     *
     * @param fileUrl the file url
     * @param onSuccess called on success
     * @param onFailure called with an exception on failure
     */
    fun deleteFile(
        fileUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

}