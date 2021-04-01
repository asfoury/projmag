package com.sdp13epfl2021.projmag

import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.database.ProjectUploader
import com.sdp13epfl2021.projmag.database.ProjectsDatabase
import com.sdp13epfl2021.projmag.model.Success
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ProjectUploaderTest {
    val mockProjectDB = Mockito.mock(ProjectsDatabase::class.java)
    val UID = "uid"
    val mockFirebaseStorage = Mockito.mock(FirebaseStorage::class.java)

    @Before
    fun setupMocks() {

    }

    @Test
    fun checkProjectAndThenUploadWorks() {
/*        ProjectUploader(mockProjectDB, UID, mockFirebaseStorage).checkProjectAndThenUpload(
            Success
        )*/
    }
}