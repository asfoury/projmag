package com.sdp13epfl2021.projmag.database

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.sdp13epfl2021.projmag.JavaToKotlinHelper
import com.sdp13epfl2021.projmag.database.impl.firebase.FirebaseMetadataDatabase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
class FirebaseMetadataDatabaseTest {
    val mockFirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
    val mockColRef = Mockito.mock(CollectionReference::class.java)
    val mockDocRef = Mockito.mock(DocumentReference::class.java)
    val mockTaskVoid: Task<Void> =
        Mockito.mock(Task::class.java) as Task<Void>
    val mockTaskDoc: Task<DocumentSnapshot> =
        Mockito.mock(Task::class.java) as Task<DocumentSnapshot>
    val mockDS: DocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)

    val videoUri = "uri"
    val lang = "en"
    val subtitles = "some subtitles, not webvtt formatted, but here it does not matter"

    @Before
    fun setupMocks() {
        Mockito.`when`(mockFirebaseFirestore.collection(FirebaseMetadataDatabase.ROOT_VIDEO))
            .thenReturn(mockColRef)

        Mockito.`when`(mockColRef.document(videoUri)).thenReturn(mockDocRef)

        Mockito.`when`(mockColRef.document(videoUri)).thenReturn(mockDocRef)

        Mockito.`when`(mockDocRef.get()).thenReturn(mockTaskDoc)

        Mockito.`when`(mockDocRef.set(JavaToKotlinHelper.anyObject())).thenReturn(mockTaskVoid)

        Mockito.`when`(mockDS.get(lang)).thenReturn(subtitles)

        Mockito.`when`(mockTaskDoc.addOnSuccessListener(JavaToKotlinHelper.anyObject())).then {
            val a = it.arguments[0] as OnSuccessListener<DocumentSnapshot>
            a.onSuccess(mockDS)
            mockTaskDoc
        }
        Mockito.`when`(mockTaskDoc.addOnFailureListener(JavaToKotlinHelper.anyObject()))
            .thenReturn(mockTaskDoc)

        Mockito.`when`(mockTaskVoid.addOnSuccessListener(JavaToKotlinHelper.anyObject()))
            .thenReturn(mockTaskVoid)
        Mockito.`when`(mockTaskVoid.addOnFailureListener(JavaToKotlinHelper.anyObject()))
            .thenReturn(mockTaskVoid)
    }

    @Test
    fun addSubtitlesToVideoWorks() {
        FirebaseMetadataDatabase(mockFirebaseFirestore).addSubtitlesToVideo(
            videoUri,
            lang,
            subtitles,
            {},
            {}
        )
    }

    @Test
    fun getSubtitlesFromVideoWorks() {
        FirebaseMetadataDatabase(mockFirebaseFirestore).getSubtitlesFromVideo(
            videoUri,
            lang,
            { subs -> assertEquals(subtitles, subs) },
            {}
        )
    }
}