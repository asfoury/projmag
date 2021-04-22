package com.sdp13epfl2021.projmag.database.fake

import android.net.Uri
import com.sdp13epfl2021.projmag.database.FileDatabase
import java.io.File
import java.util.*

class FakeFileDatabase(var files: Map<String, File> = emptyMap()) : FileDatabase {

    override fun getFile(
        fileUrl: String,
        destinationFolder: File,
        onSuccess: (File) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val remoteFile: File? = files[fileUrl]
        if (remoteFile == null) {
            onFailure(Exception("File not found"))
        } else {
            onSuccess(remoteFile)
        }
    }

    override fun pushFile(file: File, onSuccess: (Uri) -> Unit, onFailure: (Exception) -> Unit) {
        val uriString = "${UUID.randomUUID()}_${file.name}"
        files = files + (uriString to file)
        onSuccess(Uri.parse(uriString))
    }

    override fun pushFileFromUri(
        uri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteFile(
        fileUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        files = files - fileUrl
    }
}