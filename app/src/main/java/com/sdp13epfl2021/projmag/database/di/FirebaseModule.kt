package com.sdp13epfl2021.projmag.database.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Named("userID")
    @Provides
    fun provideUserID(auth: FirebaseAuth): String = auth.uid ?: ""

    @Provides
    @Named("userRootDir")
    fun provideUserRootDir(@ApplicationContext context: Context): File =
        File(context.applicationContext.filesDir, "users")

}