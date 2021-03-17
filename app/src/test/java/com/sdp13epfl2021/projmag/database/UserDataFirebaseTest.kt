package com.sdp13epfl2021.projmag.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserDataFirebaseTest {
    val ID = "some-id"

    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    val mockFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    val mockColRef = Mockito.mock(CollectionReference::class.java)
    val mockDocRef = Mockito.mock(DocumentReference::class.java)

    val database = UserDataFirebase(mockFirebaseFirestore, mockFirebaseAuth)

    @Before
    fun setupMocks() {
        Mockito
            .`when`(mockFirebaseFirestore.collection(UserDataFirebase.ROOT))
            .thenReturn(mockColRef)
        Mockito
            .`when`(mockColRef.document(UserDataFirebase.FAVORITES_FIELD))
            .thenReturn(mockDocRef)
    }
}