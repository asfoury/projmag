package com.sdp13epfl2021.projmag.database.impl.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import java.lang.Exception

/**
 * An implementation of MetadataDatabase using Firebase firestore.
 */
class FirebaseMetadataDatabase(private val firestore: FirebaseFirestore) : MetadataDatabase {
    companion object {
        const val ROOT_VIDEO = "video_metadata"
    }

    private fun discardSlash(s: String) = s.replace("/", "")

    override fun addSubtitlesToVideo(
        videoUri: String,
        language: String,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection(ROOT_VIDEO)
            .document(discardSlash(videoUri))
            .set(
                hashMapOf(
                    language to content
                )
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun getSubtitlesFromVideo(
        videoUri: String,
        language: String,
        onSuccess: (String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore
            .collection(ROOT_VIDEO)
            .document(discardSlash(videoUri))
            .get()
            .addOnSuccessListener { doc ->
                doc?.get(language)?.let { onSuccess(it as String) } ?: onSuccess(null)
            }
            .addOnFailureListener(onFailure)
    }
}