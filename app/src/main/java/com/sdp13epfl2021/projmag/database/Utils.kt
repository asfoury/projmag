package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Utils private constructor(context: Context, fb: Firebase) {
    val firestore = fb.firestore
    // Uncomment to disable firebase cache
    /* val settings = firestoreSettings {
        isPersistenceEnabled = false
    }
    firestore.firestoreSettings = settings*/

    val userDatabase: UserDataDatabase = UserDataFirebase(firestore, fb.auth)
    val candidatureDatabase: CandidatureDatabase = DummyCandidatureDatabase() //TODO implements a CandidatureDatabase (after CV/Profile are available)
    val fileDatabase: FileDatabase = FirebaseFileDatabase(fb.storage, fb.auth)
    val metadataDatabase: MetadataDatabase = MetadataFirebase(firestore)
    val projectsDatabase: CachedProjectsDatabase =
        CachedProjectsDatabase(
            OfflineProjectDatabase(
                FirebaseProjectsDatabase(
                    firestore
                ),
                File(context.filesDir, "projects")
            )
        )

    companion object {
        private var instance: Utils? = null

        @Synchronized
        fun getInstance(context: Context, fb: Firebase = Firebase): Utils {
            if (instance == null) {
                instance = Utils(context.applicationContext, fb)
            }
            return instance!!
        }
    }
}