package com.sdp13epfl2021.projmag.database

import android.net.Uri
import java.io.File
import java.util.*

class FakeFileDatabaseTest(var files: Map<String, File>) : FileDatabase {

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

    override fun deleteFile(
        fileUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        files = files - fileUrl
    }
}