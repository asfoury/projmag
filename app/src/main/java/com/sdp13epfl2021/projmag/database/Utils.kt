package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Utils(private val context: Context) {
    private val firestore = Firebase.firestore
    // Uncomment to disable firebase cache
    /* val settings = firestoreSettings {
        isPersistenceEnabled = false
    }
    firestore.firestoreSettings = settings*/

    val userDatabase: UserDataDatabase = UserDataFirebase(firestore, Firebase.auth)
    val fileDatabase: FileDatabase = FirebaseFileDatabase(Firebase.storage, Firebase.auth)
    val projectsDatabase: CachedProjectsDatabase =
        CachedProjectsDatabase(
            OfflineProjectDatabase(
                FirebaseProjectsDatabase(
                    firestore
                ),
                File(context.filesDir, "projects")
            )
        )
}