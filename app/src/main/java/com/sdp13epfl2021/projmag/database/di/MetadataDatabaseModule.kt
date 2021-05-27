package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseMetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MetadataDatabaseModule {
    @Provides
    @Singleton
    fun provideMetadataDatabase(
        fs: FirebaseFirestore
    ): MetadataDatabase =
        FirebaseMetadataDatabase(
            fs
        )
}