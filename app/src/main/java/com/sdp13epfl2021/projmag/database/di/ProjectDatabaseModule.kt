package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineProjectDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProjectDatabaseModule {
    @Provides
    @Singleton
    fun provideProjectDatabase(
        fs: FirebaseFirestore,
        @Named("fileCacheProjectDB") dir: File,
        candidatureDB: CandidatureDatabase
    ): ProjectDatabase =
        OfflineProjectDatabase(
            FirebaseProjectDatabase(fs),
            dir,
            candidatureDB
        )
}