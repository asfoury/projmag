package com.sdp13epfl2021.projmag.database

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.lang.reflect.TypeVariable
import java.nio.file.Files


class FirebaseFileDatabaseTest {

    private val UID = "some-user-id"
    private val rootUsers = "users"

    private val fileUrl = "https://example.com/firestore/a/b/310771/video_test.mp4"
    private val fileUri = Uri.parse(fileUrl)
    private val filename = "video_test.mp4"
    private val tempFolder = Files.createTempDirectory("firebase_temp_test").toFile()
    private val tempFile = File(tempFolder, filename)

    private val invalidFolder = File("/$")

    private val invalidFile = File("/$")



    private val mockFirebaseStorage = Mockito.mock(FirebaseStorage::class.java)
    private val mockRootRef = Mockito.mock(StorageReference::class.java)
    private val mockUserRef = Mockito.mock(StorageReference::class.java)
    private val mockReference = Mockito.mock(StorageReference::class.java)
    private val mockFileRef = Mockito.mock(StorageReference::class.java)
    private val mockFolder = Mockito.mock(File::class.java)
    private val mockFile = Mockito.mock(File::class.java)

    private val mockDownloadTask = Mockito.mock(FileDownloadTask::class.java)
    private val mockUploadTask = Mockito.mock(UploadTask::class.java)
    private val mockTask = Mockito.mock(Task::class.java)


    private var onSuccessGetFile: File? = null
    private val onSuccessGet: ((File) -> Unit) = { file ->
        onSuccessGetFile = file
    }

    private val onFailureNotExpected: ((Exception) -> Unit) = { e ->
        e.printStackTrace()
        assertNull(e)
        assertTrue(false)
    }

    private var onSuccessPushUri: Uri? = null
    private val onSuccessPush: ((Uri) -> Unit) = { it ->
        onSuccessPushUri = it
    }

    @Before
    fun setupMocks() {
        // destinationFolder
        Mockito
            .`when`(mockFolder.mkdirs())
            .thenReturn(true)

        Mockito
            .`when`(mockFolder.path)
            .thenReturn(tempFolder.path)

        // rootRef
        Mockito
            .`when`(mockFirebaseStorage.reference)
            .thenReturn(mockReference)
        Mockito
            .`when`(mockReference.child(rootUsers))
            .thenReturn(mockRootRef)

        // fileRef
        Mockito
            .`when`(mockFirebaseStorage.getReferenceFromUrl(fileUrl))
            .thenReturn(mockFileRef)

        // filename
        Mockito
            .`when`(mockFileRef.name)
            .thenReturn(filename)

        // destinationFile
        /*Mockito
            .`when`(File(mockFolder, Mockito.anyString()))
            .thenReturn(mockFile)*/

        // getFile
        Mockito
            .`when`(mockFileRef.getFile(Mockito.any<File>()))
            .thenReturn(mockDownloadTask)

        // listeners
        Mockito
            .`when`(mockDownloadTask.addOnSuccessListener(Mockito.any()))
            .then {
                onSuccessGet(mockFile)
                mockDownloadTask
            }
        Mockito
            .`when`(mockDownloadTask.addOnFailureListener(Mockito.any()))
            .thenReturn(mockDownloadTask)


        Mockito
            .`when`(mockRootRef.child(UID))
            .thenReturn(mockUserRef)
        Mockito
            .`when`(mockUserRef.child(filename))
            .thenReturn(mockFileRef)

        // putFile
        Mockito
            .`when`(mockFileRef.putFile(Mockito.any()))
            .thenReturn(mockUploadTask)

        //<ContinuationResultT> Task<ContinuationResultT> continueWithTask(Continuation<ResultT, Task<ContinuationResultT>> continuation)
        // continueWithTask
        Mockito
            .`when`(mockUploadTask.continueWithTask(Mockito.any<Continuation<UploadTask.TaskSnapshot, Task<Uri>>>()))
            .thenReturn(mockTask as Task<Uri>?)


        // listeners
        Mockito
            .`when`(mockTask.addOnSuccessListener(Mockito.any()))
            .then {
                onSuccessPush(fileUri)
                mockTask
            }
        Mockito
            .`when`(mockTask.addOnFailureListener(Mockito.any()))
            .thenReturn(mockTask)

        Mockito
            .`when`(mockFileRef.delete())
            .thenReturn(mockTask as Task<Void>?)


    }

    @After
    fun clean() {
        tempFolder.deleteRecursively()
        tempFolder.delete()
    }

    @Test(timeout = 1000)
    fun getFailedSuccessfullyWithInvalidFolder() {
        var e: Exception? = null
        val db = FirebaseFileDatabase(mockFirebaseStorage, UID)
        db.getFile(fileUrl, invalidFolder, { assertTrue(false) }, { it ->
            e = it
        })

        while (e == null);
        assertNotNull(e)
    }

    @Test(timeout = 1000)
    fun getWorks() {
        val db = FirebaseFileDatabase(mockFirebaseStorage, UID)
        db.getFile(fileUrl, tempFolder, onSuccessGet, onFailureNotExpected)

        while (onSuccessGetFile == null);

        assertEquals(mockFile, onSuccessGetFile)
        assertTrue(tempFolder.exists() && tempFolder.isDirectory)
        // check content ?

    }

    @Test(timeout = 1000)
    fun pushFailedSuccessfullyWithInvalidFile() {
        var e: Exception? = null
        val db = FirebaseFileDatabase(mockFirebaseStorage, UID)

        db.pushFile(invalidFile, onSuccessPush, { it ->
            e = it
        })
        while (e == null);
        assertNotNull(e)

        e = null
        db.pushFile(tempFolder, onSuccessPush, { it ->
            e = it
        })
        while (e == null);
        assertNotNull(e)
    }

    @Test(timeout = 1000)
    fun pushWorks() {
        assertTrue(tempFile.createNewFile())
        val db = FirebaseFileDatabase(mockFirebaseStorage, UID)
        db.pushFile(tempFile, onSuccessPush, onFailureNotExpected)

        while (onSuccessPushUri == null);

        assertEquals(fileUri, onSuccessPushUri)

    }

    @Test(timeout = 1000)
    fun deleteFileDoesntCrash() {
        val db = FirebaseFileDatabase(mockFirebaseStorage, UID)
        db.deleteFile(fileUrl, {}, {})
    }


}