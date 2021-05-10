package com.sdp13epfl2021.projmag.database

import android.net.Uri
import com.sdp13epfl2021.projmag.JavaToKotlinHelper
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Success
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ProjectUploaderTest {
    val mockProjectDB = Mockito.mock(ProjectDatabase::class.java)

    val mockFileDB = Mockito.mock(FileDatabase::class.java)

    val mockMetadataDB = Mockito.mock(MetadataDatabase::class.java)

    val mockCandidatureDB = Mockito.mock(CandidatureDatabase::class.java)

    val mockUri = Mockito.mock(Uri::class.java)
    val subtitles = "not formatted subtitles, but we do not really care here"

    val PID = "pid"
    val reason = "some reason"
    val exampleProject = ImmutableProject(
        id = PID,
        name = "Some test project",
        lab = "some lab",
        authorId = "some author id",
        teacher = "Some Teacher",
        TA = "Some TA",
        nbParticipant = 2,
        assigned = listOf("s1", "s2"),
        masterProject = true,
        bachelorProject = false,
        tags = listOf("t1", "t2"),
        isTaken = false,
        description = "some description"
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
            mockFileDB.pushFileFromUri(
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject()
            )
        ).then {
            val onSuccess = it.arguments[1] as ((Uri) -> Unit)
            onSuccess(mockUri)
        }

        /* MOCK_METADATA_DB */
        Mockito.`when`(
            mockMetadataDB.addSubtitlesToVideo(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                JavaToKotlinHelper.anyObject(),
                JavaToKotlinHelper.anyObject()
            )
        ).then {}

    }

    @Test
    fun checkProjectAndThenUploadWorksWithFailure() {
        ProjectUploader(
            mockProjectDB,
            mockFileDB,
            mockMetadataDB,
            mockCandidatureDB,
            { msg -> assertEquals(reason, msg) },
            {},
            {}).checkProjectAndThenUpload(
            Failure(reason),
            null,
            null
        )
    }

    @Test
    fun checkProjectAndThenUploadWorks() {
        ProjectUploader(
            mockProjectDB,
            mockFileDB,
            mockMetadataDB,
            mockCandidatureDB,
            { msg -> assertEquals("Project pushed (without video) with ID : $PID", msg) },
            {},
            {}).checkProjectAndThenUpload(
            Success(exampleProject),
            null,
            null
        )
    }

    @Test
    fun checkProjectAndThenUploadWithURIWorks() {
        ProjectUploader(
            mockProjectDB,
            mockFileDB,
            mockMetadataDB,
            mockCandidatureDB,
            { msg -> assertEquals("Project pushed with ID : $PID", msg) },
            {},
            {}).checkProjectAndThenUpload(
            Success(exampleProject),
            mockUri,
            null
        )
    }

    @Test
    fun checkProjectAndThenUploadWithURIAndSubtitlesWorks() {
        ProjectUploader(
            mockProjectDB,
            mockFileDB,
            mockMetadataDB,
            mockCandidatureDB,
            { msg -> assertEquals("Project pushed with ID : $PID", msg) },
            {},
            {}).checkProjectAndThenUpload(
            Success(exampleProject),
            mockUri,
            subtitles
        )
    }
}