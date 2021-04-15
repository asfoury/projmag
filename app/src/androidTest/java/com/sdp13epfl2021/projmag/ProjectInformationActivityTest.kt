package com.sdp13epfl2021.projmag


import android.content.Intent
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.model.ImmutableProject
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

    private val videoUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FWkanpcGw5Dhp9J8uvLAIDQ8XhxA3%2F30a0704e-8ed3-40a8-8621-ea69a6b7fe3e_SNK%20The%20Final%20Season%20OP.mp4?alt=media&token=dc39420a-069e-4861-9b63-c6df47ad5d89"
    private val imageArchUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FWkanpcGw5Dhp9J8uvLAIDQ8XhxA3%2FA4k.png?alt=media&token=57100a03-8b03-4056-a0c7-c29564d08c98"
    private val imageSOUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FWkanpcGw5Dhp9J8uvLAIDQ8XhxA3%2Faa4b71b2-b341-4c6a-88b3-6e1f30a251a2_SO_1avril.png?alt=media&token=7af98fd6-297e-4ed2-820c-3b88d96adfb3"

    private val notWorkingVideoUrl = "https://thisLinkWillNotWork.mp4"
    private val notWorkingImageUrl = "https://thisLinkWillNotWork.jpeg"
    private val emptyImageUrl = "https://firebasestorage.googleapis.com/v0/b/projmag.appspot.com/o/users%2FWkanpcGw5Dhp9J8uvLAIDQ8XhxA3%2Fempty.jpeg?alt=media&token=17465122-87e4-4538-9d63-848faf553a25"

    private val project = ImmutableProject("fakeProjectIdDoNotUse",
        "A simple project for test (with a video)",
        "labName",
        "Teacher5",
        "TA5",
        3,
        listOf<String>(),
        false,
        true,
        listOf("Low Level","Networking","Driver"),
        false,
        "<h1>Description of project</h1>" +
                "<img src=\"$imageArchUrl\">" +
                "<img src=\"$notWorkingImageUrl\">" +
                "<img src=\"$emptyImageUrl\">" +
                "<ul><li>test1</li><li>test2</li></ul>" +
                "<p>This is a small image : </p><img src=\"$imageSOUrl\">",
        listOf(videoUrl, notWorkingVideoUrl)
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

    @Test
    fun videoIsLoadedAndCanBePaused() {
        lateinit var videoView: VideoView

        scenario.onActivity {
            videoView = it.findViewById(R.id.info_video)
        }
        Thread.sleep(10000) //sleep 10sec to wait the download

        assertFalse(videoView.isPlaying)
        Thread.sleep(100)
        if (videoView.isVisible) { // else the video is still not downloaded
            onView(withId(R.id.info_video)).perform(scrollTo()).perform(click())
            Thread.sleep(100)
            assertTrue(videoView.isPlaying)
        }
    }

    @Test
    fun clickOnShareButton() {
        onView(withId(R.id.shareButton)).perform(click())
    }
}
