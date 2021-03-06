package com.sdp13epfl2021.projmag.database.di

import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedUserdataDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseUserdataDatabase
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
object UserdataDatabaseModule {
    @Provides
    @Singleton
    fun providesUserdataDatabase(
        userDB: FirebaseUserdataDatabase,
        @Named("currentUserId") uid: String,
        @Named("fileCacheUserDB") dir: File
    ): UserdataDatabase =
        OfflineCachedUserdataDatabase(
            db = userDB,
            localUserID = uid,
            usersRootDir = dir
        )

}
