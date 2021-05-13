package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    /**
     * this Provides an instance of UserDataDatabase
     */
    @Provides
    @Singleton
    fun provideUserdataDatabase(
        ff: FirebaseFirestore,
        auth: FirebaseAuth,
        @Named("fileCacheUserDB") fc: File
    ): UserdataDatabase = OfflineCachedUserdataDatabase(
        FirebaseUserdataDatabase(ff, auth),
        auth.uid ?: "",
        fc
    )
}
