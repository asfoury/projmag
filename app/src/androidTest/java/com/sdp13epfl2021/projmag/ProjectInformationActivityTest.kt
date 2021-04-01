package com.sdp13epfl2021.projmag

/*
import android.content.Intent
import android.widget.VideoView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.database.*
import com.sdp13epfl2021.projmag.model.ImmutableProject
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@RunWith(AndroidJUnit4::class)
class ProjectInformationActivityTest {

    private val videoUrl = "https://fakeVideoLink/abc?def"
    private val imageArchUrl = "https://fakeImageLink/Arch Linux > Debian/4K"
    private val imageSOUrl = "https://fakeImageLink/StackOverflowOn1Avril"

    private val notWorkingVideoUrl = "https://thisLinkWillNotWork.mp4"
    private val notWorkingImageUrl = "https://thisLinkWillNotWork.jpeg"
    private val emptyImageUrl = "https://thisFicheIsEmpty.jpeg"

    private val project = ImmutableProject("00000",
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
                "<ul><li>test1</li><li>test2</li></ul>" +
                "<p>This is a small image : </p><img src=\"$imageSOUrl\">" +
                "<video>$videoUrl</video>" +
                "<video>$videoUrl</video>" +
                "<video>$notWorkingVideoUrl</video>"
    )

    private lateinit var intent: Intent

    private val inputStreamSNK = javaClass.classLoader!!.getResourceAsStream("30a0704e-8ed3-40a8-8621-ea69a6b7fe3e_SNK The Final Season OP.mp4")
    private val inputStreamArch = javaClass.classLoader!!.getResourceAsStream("A4k.png")
    private val inputStreamSO = javaClass.classLoader!!.getResourceAsStream("SO_1avril.png")

    private val videoFile: File = Files.createTempFile("SNK OP", ".mp4").toFile()
    private val imageArchFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val imageSOFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val emptyFile: File = Files.createTempFile("empty", ".png").toFile()

    lateinit var scenario: ActivityScenario<ProjectInformationActivity>

    @Before
    fun setup() {
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

        intent = Intent(ApplicationProvider.getApplicationContext(), ProjectInformationActivity::class.java)
        intent.putExtra("project", project)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        videoFile.delete()
        imageArchFile.delete()
        imageSOFile.delete()
    }

    @Test
    fun videoIsLoadedAndCanBePaused() {
        lateinit var videoView: VideoView

        scenario.onActivity {
            videoView = it.findViewById(R.id.info_video)
        }
        assertFalse(videoView.isPlaying)
        Thread.sleep(1000)
        onView(withId(R.id.info_scroll_view)).perform(ViewActions.swipeUp())
        Thread.sleep(1000)

        onView(withId(R.id.info_video)).perform(click())
        Thread.sleep(1000)
        assertTrue(videoView.isPlaying)
    }
}
*/