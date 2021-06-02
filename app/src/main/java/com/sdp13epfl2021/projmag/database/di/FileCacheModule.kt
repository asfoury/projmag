package com.sdp13epfl2021.projmag.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object FileCacheModule {
    @Provides
    @Named("fileCacheUserDB")
    fun fileForCacheUserDB(@ApplicationContext applicationContext: Context): File =
        File(applicationContext.filesDir, "users")

    @Provides
    @Named("fileCacheProjectDB")
    fun fileForCacheProjectDB(@ApplicationContext applicationContext: Context): File =
        File(applicationContext.filesDir, "projects")

}