package com.sdp13epfl2021.projmag.database.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseUserdataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserdataDatabaseModule {
    @Binds
    @Singleton
    abstract fun bindUserdataDatabase(
        userDB: FirebaseUserdataDatabase
    ): UserdataDatabase
}


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}
