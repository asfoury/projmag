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
    fun fileForCache(@ApplicationContext applicationContext: Context): File =
        File(applicationContext.filesDir, "users")

}