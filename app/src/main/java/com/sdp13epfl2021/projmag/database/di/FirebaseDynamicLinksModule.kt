package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseDynamicLinksModule {
    @Provides
    @Singleton
    fun provideFirebaseDynamicLinks(): FirebaseDynamicLinks = FirebaseDynamicLinks.getInstance()
}