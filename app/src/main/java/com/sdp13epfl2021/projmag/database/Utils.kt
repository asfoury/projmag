package com.sdp13epfl2021.projmag.database

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sdp13epfl2021.projmag.database.impl.cache.CachedProjectDatabase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedCandidatureDatabase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedUserdataDatabase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineProjectDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.*
import com.sdp13epfl2021.projmag.database.interfaces.*
import java.io.File

class Utils(
    val auth: FirebaseAuth,
    val userdataDatabase: UserdataDatabase,
    val candidatureDatabase: CandidatureDatabase,
    val fileDatabase: FileDatabase,
    val metadataDatabase: MetadataDatabase,
    val projectDatabase: ProjectDatabase,
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
            userdataDB: UserdataDatabase = createUserDB(context, auth, reset),
            candidatureDB: CandidatureDatabase = createCandidatureDB(
                context,
                auth,
                userdataDB,
                reset
            ),
            fileDB: FileDatabase = FirebaseFileDatabase(Firebase.storage, auth),
            metadataDB: MetadataDatabase = FirebaseMetadataDatabase(Firebase.firestore),
            projectDB: ProjectDatabase = createProjectDB(context, candidatureDB, reset)
        ): Utils {
            if (instance == null || reset) {
                instance = Utils(auth, userdataDB, candidatureDB, fileDB, metadataDB, projectDB)
            }
            return instance!!
        }

        private fun createProjectDB(
            context: Context,
            candidatureDB: CandidatureDatabase,
            reset: Boolean
        ): ProjectDatabase {
            return if (reset || instance?.projectDatabase == null) {
                CachedProjectDatabase(
                    OfflineProjectDatabase(
                        FirebaseProjectDatabase(
                            Firebase.firestore
                        ),
                        getSubDir(context, "projects"),
                        candidatureDB
                    )
                )
            } else {
                instance!!.projectDatabase
            }
        }

        private fun createUserDB(
            context: Context,
            auth: FirebaseAuth,
            reset: Boolean
        ): UserdataDatabase {
            return if (reset || instance?.userdataDatabase == null) {
                OfflineCachedUserdataDatabase(
                    FirebaseUserdataDatabase(
                        Firebase.firestore,
                        auth
                    ),
                    auth.currentUser?.uid ?: "",
                    getSubDir(context, "users")
                )
            } else {
                instance!!.userdataDatabase
            }
        }

        private fun createCandidatureDB(
            context: Context,
            auth: FirebaseAuth,
            userdataDB: UserdataDatabase,
            reset: Boolean
        ): CandidatureDatabase {
            return if (reset || instance?.candidatureDatabase == null) {
                OfflineCachedCandidatureDatabase(
                    FirebaseCandidatureDatabase(Firebase.firestore, auth, userdataDB),
                    getSubDir(context, "projects")
                )
            } else {
                instance!!.candidatureDatabase
            }
        }

        private fun getSubDir(context: Context, subName: String): File {
            return File(context.applicationContext.filesDir, subName)
        }
    }
}