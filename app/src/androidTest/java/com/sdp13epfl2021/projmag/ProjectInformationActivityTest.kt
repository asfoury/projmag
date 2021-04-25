package com.sdp13epfl2021.projmag


import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.UiThreadTestRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.database.fake.*
import com.sdp13epfl2021.projmag.model.ImmutableProject
import junit.framework.Assert.*
import org.hamcrest.Description
import junit.framework.Assert.*
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProjectInformationActivityTest {

    private val epflUrl =
        "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fc1758313-31b0-4880-8381-4723c17ae9e4?alt=media&token=64a07385-6ec4-4d90-8e56-293d409cb026"
    private val snkUrl =
        "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2F641a85c7-2944-4ffa-a18f-7ee5cf501df5?alt=media&token=32b1a560-7ae9-4f01-ba6b-fdfb8748f21c"
    private val imageArchUrl =
        "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fec855899-e73b-4977-a3f8-054f38e966ed_Arch_4k.png?alt=media&token=db39b754-8537-4d8c-a854-abd6a3cadf1b"
    private val imageSOUrl =
        "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2F70e3321e-670d-47a1-9b36-25a6d72ad3b7_SO.png?alt=media&token=7c90acdc-5a4b-4594-a0fc-0d35b9e50b6a"

    private val notWorkingVideoUrl = "https://thisLinkWillNotWork.mp4"
    private val notWorkingImageUrl = "https://thisLinkWillNotWork.jpeg"
    private val emptyImageUrl =
        "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fea77c6b0-0f93-4b25-80ae-808fc6d70c78_empty.jpeg?alt=media&token=11762a74-a0b9-4ba8-aed3-e82fb981dab0"

    private val pid = "fakeProjectIdDoNotUse"
    private val userID = "fakeUserID"
    private val project = ImmutableProject(
        pid,
        "A simple project for test (with a video)",
        "labName",
        userID,
        "Teacher5",
        "TA5",
        3,
        listOf<String>(),
        false,
        true,
        listOf("Low Level", "Networking", "Driver"),
        false,
        "<h1>Description of project</h1>" +
                "<img src=\"$imageArchUrl\">" +
                "<img src=\"$notWorkingImageUrl\">" +
                "<img src=\"$emptyImageUrl\">" +
                "<ul><li>test1</li><li>test2</li></ul>" +
                "<p>This is a small image : </p><img src=\"$imageSOUrl\">",
        listOf(epflUrl, snkUrl, notWorkingVideoUrl)
    )


    private val inputStreamSNK = javaClass.classLoader!!.getResourceAsStream("641a85c7-2944-4ffa-a18f-7ee5cf501df5")
    private val inputStreamEPFL = javaClass.classLoader!!.getResourceAsStream("c1758313-31b0-4880-8381-4723c17ae9e4")
    private val inputStreamArch = javaClass.classLoader!!.getResourceAsStream("ec855899-e73b-4977-a3f8-054f38e966ed_Arch_4k.png")
    private val inputStreamSO = javaClass.classLoader!!.getResourceAsStream("70e3321e-670d-47a1-9b36-25a6d72ad3b7_SO.png")
    private val inputStreamEmpty = javaClass.classLoader!!.getResourceAsStream("ea77c6b0-0f93-4b25-80ae-808fc6d70c78_empty.jpeg")

    private val videoSNKFile: File = Files.createTempFile("SNK OP ", ".mp4").toFile()
    private val videoEPFLFile: File = Files.createTempFile("EPFL", ".mkv").toFile()
    private val imageArchFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val imageSOFile: File = Files.createTempFile("SO", ".png").toFile()
    private val emptyFile: File = Files.createTempFile("empty", ".jpeg").toFile()

    private val projectsDB = FakeProjectsDatabase(listOf(project))
    private val userdataDB = FakeUserDataDatabase()
    private val fileDB = FakeFileDatabase(
        mapOf(
            snkUrl to videoSNKFile,
            epflUrl to videoEPFLFile,
            imageArchUrl to imageArchFile,
            imageSOUrl to imageSOFile,
            emptyImageUrl to emptyFile
        )
    )

    private val auth: FirebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    private val user: FirebaseUser = Mockito.mock(FirebaseUser::class.java)

    private val candidatureDB = FakeCandidatureDatabase()
    private val metadataDB = FakeMetadataDatabase()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val utils = Utils.getInstance(context, true, auth, userdataDB, candidatureDB, fileDB, metadataDB, projectsDB)

    private fun getIntent(): Intent {
        val intent = Intent(context, ProjectInformationActivity::class.java)
        intent.putExtra(MainActivity.projectString, project)

        Mockito
            .`when`(auth.currentUser)
            .thenReturn(user)
        Mockito
            .`when`(user.uid)
            .thenReturn(userID)

        Files.copy(inputStreamSNK, videoSNKFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamEPFL, videoEPFLFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamArch, imageArchFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamSO, imageSOFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamEmpty, emptyFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        return intent
    }

    @get:Rule var activityScenarioRule = ActivityScenarioRule<ProjectInformationActivity>(getIntent())


    @After
    fun clean() {
        videoSNKFile.delete()
        videoEPFLFile.delete()
        imageArchFile.delete()
        imageSOFile.delete()
        emptyFile.delete()
        Utils.getInstance(context, true)
    }

    //it should play the first video, continue with next, play prev, play next, pause
    @Test
    fun videoIsLoadedAndCanBePaused() {
        lateinit var videoView: VideoView
        lateinit var scrollView: ScrollView

        activityScenarioRule.scenario.onActivity {
            videoView = it.findViewById(R.id.info_video)
            scrollView = it.findViewById(R.id.info_scroll_view)
        }

        Thread.sleep(5000) //sleep 10sec to wait the download
        assertFalse(videoView.isPlaying)
        assertFalse(videoView.isPlaying)
        Thread.sleep(100)

        val video = onView(withId(R.id.info_video))
        video.perform(scrollTo())
        Thread.sleep(500)
        video.perform(click())
        Thread.sleep(500)
        assertTrue(videoView.isPlaying)
        Thread.sleep(6000)
        assertTrue(videoView.isPlaying)

        val prevButton = onView(
            Matchers.allOf(
                withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Previous track")
            )
        )
        prevButton.perform(click())
        Thread.sleep(100)
        prevButton.perform(click())
        Thread.sleep(100)
        assertTrue(videoView.isPlaying)

        val nextButton = onView(
            Matchers.allOf(
                withClassName(Matchers.`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Next track")
            )
        )
        nextButton.perform(click())
        Thread.sleep(100)
        assertTrue(videoView.isPlaying)

        nextButton.perform(click())
        Thread.sleep(100)
        assertTrue(videoView.isPlaying)

        Thread.sleep(3100)
        video.perform(click())
        Thread.sleep(100)
        assertFalse(videoView.isPlaying)

    }

    @Test
    fun clickOnShareButton() {
        onView(withId(R.id.shareButton)).perform(click())
    }

    @Test
    fun clickOnWaitingListButton() {
        onView(withId(R.id.waitingListButton)).perform(click())
    }

}





