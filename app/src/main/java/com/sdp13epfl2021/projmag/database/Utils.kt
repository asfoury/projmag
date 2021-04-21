package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Utils(
    val userDataDatabase: UserDataDatabase,
    val fileDatabase: FileDatabase,
    val metadataDatabase: MetadataDatabase,
    val projectsDatabase: CachedProjectsDatabase,
) {

    companion object {
        private var instance: Utils? = null

        @Synchronized
        fun getInstance(
            context: Context,
            userDataDB: UserDataDatabase = UserDataFirebase(Firebase.firestore, Firebase.auth),
            fileDB: FileDatabase = FirebaseFileDatabase(Firebase.storage, Firebase.auth),
            metadataDB: MetadataDatabase = MetadataFirebase(Firebase.firestore),
            projectsDB: CachedProjectsDatabase = CachedProjectsDatabase(
                OfflineProjectDatabase(
                    FirebaseProjectsDatabase(
                        Firebase.firestore
                    ),
                    File(context.applicationContext.filesDir, "projects")
                )
            )
        ): Utils {
            if (instance == null) {
                instance = Utils(userDataDB, fileDB, metadataDB, projectsDB)
            }
            return instance!!
        }
    }
}