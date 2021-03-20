package com.sdp13epfl2021.projmag.database

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

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
        lateinit var fileRef: StorageReference
        lateinit var destinationFile: File

        try {
            if (!destinationFolder.mkdirs() && !destinationFolder.isDirectory) {
                throw IOException("The destination folder \"${destinationFolder.path}\" can't be created.")
            }
            fileRef = storage.getReferenceFromUrl(fileUrl)
            destinationFile = File(destinationFolder, fileRef.name)
        } catch (e: Exception) {
            GlobalScope.launch { onFailure(e) }
            return
        }

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