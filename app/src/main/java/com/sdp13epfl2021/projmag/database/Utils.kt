package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Utils(
    val auth: FirebaseAuth,
    val userDataDatabase: UserDataDatabase,
    val candidatureDatabase: CandidatureDatabase,
    val fileDatabase: FileDatabase,
    val metadataDatabase: MetadataDatabase,
    val projectsDatabase: ProjectsDatabase,
    val userProfileDatabase: UserProfileDatabase
) {

    companion object {
        private var instance: Utils? = null

        // Uncomment to disable Firebase cache
        /*init {
            val settings = firestoreSettings {
                isPersistenceEnabled = false
            }
            Firebase.firestore.firestoreSettings = settings
        }*/

        @Synchronized
        fun getInstance(
            context: Context,
            reset: Boolean = false,
            auth: FirebaseAuth = Firebase.auth,
            userDataDB: UserDataDatabase = UserDataFirebase(Firebase.firestore, auth),
            candidatureDB: CandidatureDatabase = FirebaseCandidatureDatabase(
                Firebase.firestore,
                auth,
                userDataDB
            ),
            fileDB: FileDatabase = FirebaseFileDatabase(Firebase.storage, auth),
            metadataDB: MetadataDatabase = MetadataFirebase(Firebase.firestore),
            projectsDB: ProjectsDatabase? = null,
            userProfileDatabase: UserProfileDatabase = UserProfileDatabase(Firebase.firestore, auth)//avoid creating an offline database every time the function is called
        ): Utils {
            if (instance == null || reset) {
                instance = Utils(
                    auth,
                    userDataDB,
                    candidatureDB,
                    fileDB,
                    metadataDB, projectsDB ?: createProjectsDB(context),
                    userProfileDatabase
                )
            }
            return instance!!
        }

        private fun createProjectsDB(context: Context): ProjectsDatabase {
            return CachedProjectsDatabase(
                OfflineProjectDatabase(
                    FirebaseProjectsDatabase(
                        Firebase.firestore
                    ),
                    File(context.applicationContext.filesDir, "projects")
                )
            )
        }
    }
}