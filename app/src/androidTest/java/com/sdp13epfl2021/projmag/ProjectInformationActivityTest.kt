package com.sdp13epfl2021.projmag


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
import androidx.test.filters.LargeTest
import androidx.test.rule.UiThreadTestRule
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.model.ImmutableProject
import junit.framework.Assert.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProjectInformationActivityTest {

    /*
    private val videoUrl = "https://fakeVideoLink/abc?def"
    private val imageArchUrl = "https://fakeImageLink/Arch Linux > Debian/4K"
    private val imageSOUrl = "https://fakeImageLink/StackOverflowOn1Avril"
    */

    private val epflUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fc1758313-31b0-4880-8381-4723c17ae9e4?alt=media&token=64a07385-6ec4-4d90-8e56-293d409cb026"
    private val snkUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2F641a85c7-2944-4ffa-a18f-7ee5cf501df5?alt=media&token=32b1a560-7ae9-4f01-ba6b-fdfb8748f21c"
    private val imageArchUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fec855899-e73b-4977-a3f8-054f38e966ed_Arch_4k.png?alt=media&token=db39b754-8537-4d8c-a854-abd6a3cadf1b"
    private val imageSOUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2F70e3321e-670d-47a1-9b36-25a6d72ad3b7_SO.png?alt=media&token=7c90acdc-5a4b-4594-a0fc-0d35b9e50b6a"

    private val notWorkingVideoUrl = "https://thisLinkWillNotWork.mp4"
    private val notWorkingImageUrl = "https://thisLinkWillNotWork.jpeg"
    private val emptyImageUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FFakeUserFolderForTestingOnly%2Fea77c6b0-0f93-4b25-80ae-808fc6d70c78_empty.jpeg?alt=media&token=11762a74-a0b9-4ba8-aed3-e82fb981dab0"

    private val project = ImmutableProject(
        "fakeProjectIdDoNotUse",
        "A simple project for test (with a video)",
        "labName",
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


    private lateinit var intent: Intent

    /*
    private val inputStreamSNK = javaClass.classLoader!!.getResourceAsStream("30a0704e-8ed3-40a8-8621-ea69a6b7fe3e_SNK The Final Season OP.mp4")
    private val inputStreamArch = javaClass.classLoader!!.getResourceAsStream("A4k.png")
    private val inputStreamSO = javaClass.classLoader!!.getResourceAsStream("SO_1avril.png")

    private val videoFile: File = Files.createTempFile("SNK OP", ".mp4").toFile()
    private val imageArchFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val imageSOFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val emptyFile: File = Files.createTempFile("empty", ".png").toFile()
    */
    lateinit var scenario: ActivityScenario<ProjectInformationActivity>

    @Before
    fun setup() {
        /*
        Files.copy(inputStreamSNK, videoFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamArch, imageArchFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamSO, imageSOFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        //TODO find a way to mock the database
        Utils.projectsDatabase = CachedProjectsDatabase(FakeDatabaseTest(listOf(project)))
        Utils.fileDatabase = FakeFileDatabaseTest(
            mapOf(
                videoUrl to videoFile,
                imageArchUrl to imageArchFile,
                imageSOUrl to imageSOFile,
                emptyImageUrl to emptyFile
            )
        )
        */

        intent = Intent(ApplicationProvider.getApplicationContext(), ProjectInformationActivity::class.java)
        intent.putExtra("project", project)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        /*
        videoFile.delete()
        imageArchFile.delete()
        imageSOFile.delete()
        */
    }

    //it should play the first video, continue with next, play prev, play next, pause
    @Test
    fun videoIsLoadedAndCanBePaused() {
        lateinit var videoView: VideoView
        lateinit var scrollView: ScrollView

        scenario.onActivity {
            videoView = it.findViewById(R.id.info_video)
            scrollView = it.findViewById(R.id.info_scroll_view)
        }

        Thread.sleep(10000) //sleep 10sec to wait the download
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

        /*val prevButton = onView(
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
        assertFalse(videoView.isPlaying)*/

    }

}
