package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseFileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileDatabaseModule {
    @Provides
    @Singleton
    fun provideFileDatabase(
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): FileDatabase =
        FirebaseFileDatabase(
            storage,
            auth
        )
}