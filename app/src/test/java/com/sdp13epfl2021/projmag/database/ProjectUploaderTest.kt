package com.sdp13epfl2021.projmag.database

import android.net.Uri
import androidx.core.net.toFile
import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.JavaToKotlinHelper
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Success
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.File
import kotlin.math.PI

class ProjectUploaderTest {
    val mockProjectDB = Mockito.mock(ProjectsDatabase::class.java)

    val mockFileDatabase = Mockito.mock(FileDatabase::class.java)

    val mockUri = Mockito.mock(Uri::class.java)

    val PID = "pid"
    val reason = "some reason"
    val exampleProject = ImmutableProject(
        id = PID,
        name = "Some test project",
        description = "some description",
        tags = listOf("t1", "t2"),
        isTaken = false,
        bachelorProject = false,
        masterProject = true,
        assigned = listOf("s1", "s2"),
        nbParticipant = 2,
        TA = "Some TA",
        teacher = "Some Teacher",
        lab = "some lab"
    )

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setupMocks() {
        /* MOCK_PROJECT_DB */
        Mockito
            .`when`(
                mockProjectDB.pushProject(
                    JavaToKotlinHelper.anyObject(),
                    JavaToKotlinHelper.anyObject(),
                    JavaToKotlinHelper.anyObject()
                )
            )
            .then {
                val onSuccess = it.arguments[1] as ((String) -> Unit)
                onSuccess(PID)
            }

        Mockito.`when`(
            mockProjectDB.updateVideoWithProject(
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject()
            )
        ).then {
            val onSuccess = it.arguments[2] as (() -> Unit)
            onSuccess()
        }

        /* MOCK_FILE_DB */
        Mockito.`when`(
            mockFileDatabase.pushFileFromUri(
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject()
            )
        ).then {
            val onSuccess = it.arguments[1] as ((Uri) -> Unit)
            onSuccess(mockUri)
        }

    }

    @Test
    fun checkProjectAndThenUploadWorksWithFailure() {
        ProjectUploader(
            mockProjectDB,
            mockFileDatabase,
            { msg -> assertEquals(reason, msg) },
            {},
            {}).checkProjectAndThenUpload(
            Failure(reason),
            null,
        )
    }

    @Test
    fun checkProjectAndThenUploadWorks() {
        ProjectUploader(
            mockProjectDB,
            mockFileDatabase,
            { msg -> assertEquals("Project pushed (without video) with ID : $PID", msg) },
            {},
            {}).checkProjectAndThenUpload(
            Success(exampleProject),
            null
        )
    }

    @Test
    fun checkProjectAndThenUploadWithURIWorks() {
        ProjectUploader(
            mockProjectDB,
            mockFileDatabase,
            { msg -> assertEquals("Project pushed with ID : $PID", msg) },
            {},
            {}).checkProjectAndThenUpload(
            Success(exampleProject),
            mockUri
        )
    }
}