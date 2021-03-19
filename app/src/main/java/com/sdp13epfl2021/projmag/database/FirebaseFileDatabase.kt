package com.sdp13epfl2021.projmag.database

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.File

class FirebaseFileDatabase(
    private val storage: FirebaseStorage,
    private val userUID: String
) : FileDatabase {

    /**
     * Reference of the root of users in Firebase Storage
     */
    private val rootRef = storage.reference.child("users")


    override fun getFile(
        fileUrl: String,
        destinationFolder: File,
        onSuccess: (File) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        destinationFolder.mkdirs()
        //TODO check if the folder is valid and/or if the file can be created

        val fileRef = storage.getReferenceFromUrl(fileUrl)
        val destinationFile = File(destinationFolder, fileRef.name)

        storage
            .getReferenceFromUrl(fileUrl)
            .getFile(destinationFile)
            .addOnSuccessListener { onSuccess(destinationFile) }
            .addOnFailureListener(onFailure)
    }


    override fun pushFile(
        file: File,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileRef = rootRef
            .child(userUID)
            .child(file.name)

        fileRef
            .putFile(file.toUri())
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }


    override fun deleteFile(
        fileUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        storage
            .getReferenceFromUrl(fileUrl)
            .delete()
            .addOnSuccessListener{ onSuccess() }
            .addOnFailureListener(onFailure)
    }
}