package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedCandidatureDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseCandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CandidatureDatabaseModule {
    @Provides
    @Singleton
    fun provideCandidatureDabatbase(
        auth: FirebaseAuth,
        userdataDB: UserdataDatabase,
        @Named("fileCacheProjectDB") dir: File
    ): CandidatureDatabase =
        OfflineCachedCandidatureDatabase(
            FirebaseCandidatureDatabase(Firebase.firestore, auth, userdataDB),
            dir
        )
}