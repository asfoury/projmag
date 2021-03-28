package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

object Utils {

    lateinit var projectsDatabase: CachedProjectsDatabase
    lateinit var userDatabase: UserDataDatabase
    lateinit var fileDatabase: FileDatabase

    private var initalized = false

    fun init(context: Context) {
        if (!initalized) {
            val firestore = Firebase.firestore

            // Uncomment to disable firebase cache
            /* val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        firestore.firestoreSettings = settings*/

            projectsDatabase =
                CachedProjectsDatabase(
                    OfflineProjectDatabase(
                        FirebaseProjectsDatabase(
                            firestore
                        ),
                        File(context.filesDir, "projects")
                    )
                )
            fileDatabase = FirebaseFileDatabase(Firebase.storage, Firebase.auth)
            userDatabase = UserDataFirebase(firestore, Firebase.auth)
            initalized = true
        }
    }
}