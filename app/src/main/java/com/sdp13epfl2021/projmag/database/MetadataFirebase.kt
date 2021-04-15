package com.sdp13epfl2021.projmag.database

import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class MetadataFirebase(private val firestore: FirebaseFirestore) : MetadataDatabase {
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