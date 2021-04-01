package com.sdp13epfl2021.projmag.database

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

class FirebaseFileDatabase(
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
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
        val fileRef: StorageReference
        val destinationFile: File

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

        if (destinationFile.exists()) {
            GlobalScope.launch { onSuccess(destinationFile) }
            return
        }

        fileRef
            .getFile(destinationFile)
            .addOnSuccessListener { onSuccess(destinationFile) }
            .addOnFailureListener(onFailure)
    }


    override fun pushFile(
        file: File,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (!file.exists() || !file.isFile) {
            GlobalScope.launch { onFailure(IOException("The file \"${file.path}\" can't be read.")) }
            return
        }

        val user = auth.currentUser
        if (user == null) {
            GlobalScope.launch { onFailure(SecurityException("Pushing file can only be done by authenticated user.")) }
            return
        }

        val fileRef = rootRef
            .child(user.uid)
            .child("${UUID.randomUUID()}_${file.name}")

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