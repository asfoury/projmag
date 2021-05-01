package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
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
            userDataDB: UserDataDatabase = createUserDB(context, auth, reset),
            candidatureDB: CandidatureDatabase = createCandidatureDB(context, auth, userDataDB, reset),
            fileDB: FileDatabase = FirebaseFileDatabase(Firebase.storage, auth),
            metadataDB: MetadataDatabase = MetadataFirebase(Firebase.firestore),
            projectsDB: ProjectsDatabase = createProjectsDB(context, reset)
        ): Utils {
            if (instance == null || reset) {
                instance = Utils(auth, userDataDB, candidatureDB, fileDB, metadataDB, projectsDB)
            }
            return instance!!
        }

        private fun createProjectsDB(context: Context, reset: Boolean): ProjectsDatabase {
            return if (reset || instance?.projectsDatabase == null) {
                CachedProjectsDatabase(
                    OfflineProjectDatabase(
                        FirebaseProjectsDatabase(
                            Firebase.firestore
                        ),
                        File(context.applicationContext.filesDir, "projects")
                    )
                )
            } else {
                instance!!.projectsDatabase
            }
        }

        private fun createUserDB(context: Context, auth: FirebaseAuth, reset: Boolean): UserDataDatabase {
            return if (reset || instance?.userDataDatabase == null) {
                OfflineCachedUserDataDatabase(
                    UserDataFirebase(
                        Firebase.firestore,
                        auth
                    ),
                    auth.currentUser?.uid ?: "",
                    File(context.applicationContext.filesDir, "users")
                )
            } else {
                instance!!.userDataDatabase
            }
        }

        private fun createCandidatureDB(context: Context, auth: FirebaseAuth, userDataDB: UserDataDatabase, reset: Boolean): CandidatureDatabase {
            return if (reset || instance?.candidatureDatabase == null) {
                OfflineCachedCandidatureDatabase(
                    FirebaseCandidatureDatabase(Firebase.firestore, auth, userDataDB),
                    context.applicationContext.filesDir
                )
            } else {
                instance!!.candidatureDatabase
            }
        }
    }
}