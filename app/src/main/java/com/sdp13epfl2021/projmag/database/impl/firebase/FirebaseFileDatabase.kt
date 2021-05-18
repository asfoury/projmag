package com.sdp13epfl2021.projmag.database.impl.firebase

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

/**
 * An implementation of FileDatabase using Firebase storage.
 */
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
                GlobalScope.launch { onFailure(IOException("The destination folder \"${destinationFolder.path}\" can't be created.")) }
                return
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

        pushFileFromUriWithName(
            file.toUri(),
            "${UUID.randomUUID()}_${file.name}",
            onSuccess,
            onFailure
        )
    }

    override fun pushFileFromUri(
        uri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        pushFileFromUriWithName(
            uri,
            UUID.randomUUID().toString(),
            onSuccess,
            onFailure
        )
    }

    private fun pushFileFromUriWithName(
        uri: Uri,
        filename: String,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val user = auth.currentUser
        if (user == null) {
            GlobalScope.launch { onFailure(SecurityException("Pushing file can only be done by authenticated user.")) }
            return
        }

        val fileRef = rootRef
            .child(user.uid)
            .child(filename)

        fileRef
            .putFile(uri)
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
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun getFileName(fileUrl: String): String {
        return storage.getReferenceFromUrl(fileUrl).name
    }

}
