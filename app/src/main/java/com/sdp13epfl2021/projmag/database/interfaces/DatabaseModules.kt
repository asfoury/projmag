package com.sdp13epfl2021.projmag.database.interfaces

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.impl.cache.OfflineCachedUserdataDatabase
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseUserdataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModules {

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

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    @Named("currentUserId")
    fun provideCurrentUserID(auth: FirebaseAuth): String = auth.uid ?: ""

    @Provides
    @Named("fileCacheUserDB")
    fun fileForCache(@ApplicationContext applicationContext: Context): File =
        File(applicationContext.filesDir, "users")

}
