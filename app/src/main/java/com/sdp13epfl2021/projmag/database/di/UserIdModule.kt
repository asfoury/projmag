package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserIdModule {
    @Provides
    @Named("currentUserId")
    fun provideCurrentUserID(auth: FirebaseAuth): String = auth.uid ?: ""
}
