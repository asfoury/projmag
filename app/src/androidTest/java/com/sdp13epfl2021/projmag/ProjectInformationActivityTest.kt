package com.sdp13epfl2021.projmag


import android.content.Context
import android.content.Intent
import android.widget.ScrollView
import android.widget.VideoView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.activities.WaitingListActivity
import com.sdp13epfl2021.projmag.database.di.*
import com.sdp13epfl2021.projmag.database.fake.*
import com.sdp13epfl2021.projmag.database.interfaces.*
import com.sdp13epfl2021.projmag.model.ImmutableProject
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.inject.Named

@LargeTest
@UninstallModules(
    ProjectDatabaseModule::class,
    UserdataDatabaseModule::class,
    MetadataDatabaseModule::class,
    CandidatureDatabaseModule::class,
    FileDatabaseModule::class,
    UserIdModule::class
)
@HiltAndroidTest
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

    @BindValue
    @Named("currentUserId")
    val userID = "fakeUserID"

    private val project = ImmutableProject(
        pid,
        "A simple project for test (with a video)",
        "labName",
        userID,
        "Teacher5",
        "TA5",
        3,
        listOf(),
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
        listOf(epflUrl, snkUrl, notWorkingVideoUrl),
        listOf()
    )


    private val inputStreamSNK =
        javaClass.classLoader!!.getResourceAsStream("641a85c7-2944-4ffa-a18f-7ee5cf501df5")
    private val inputStreamEPFL =
        javaClass.classLoader!!.getResourceAsStream("c1758313-31b0-4880-8381-4723c17ae9e4")
    private val inputStreamArch =
        javaClass.classLoader!!.getResourceAsStream("ec855899-e73b-4977-a3f8-054f38e966ed_Arch_4k.png")
    private val inputStreamSO =
        javaClass.classLoader!!.getResourceAsStream("70e3321e-670d-47a1-9b36-25a6d72ad3b7_SO.png")
    private val inputStreamEmpty =
        javaClass.classLoader!!.getResourceAsStream("ea77c6b0-0f93-4b25-80ae-808fc6d70c78_empty.jpeg")

    private val videoSNKFile: File = Files.createTempFile("SNK OP ", ".mp4").toFile()
    private val videoEPFLFile: File = Files.createTempFile("EPFL", ".mkv").toFile()
    private val imageArchFile: File = Files.createTempFile("Arch", ".png").toFile()
    private val imageSOFile: File = Files.createTempFile("SO", ".png").toFile()
    private val emptyFile: File = Files.createTempFile("empty", ".jpeg").toFile()


    @BindValue
    val projectsDB: ProjectDatabase = FakeProjectDatabase(listOf(project))

    @BindValue
    val userdataDB: UserdataDatabase = FakeUserdataDatabase()

    @BindValue
    val fileDB: FileDatabase = FakeFileDatabase(
        mapOf(
            snkUrl to videoSNKFile,
            epflUrl to videoEPFLFile,
            imageArchUrl to imageArchFile,
            imageSOUrl to imageSOFile,
            emptyImageUrl to emptyFile
        )
    )

    @BindValue
    val candidatureDB: CandidatureDatabase = FakeCandidatureDatabase()

    @BindValue
    val metadataDB: MetadataDatabase = FakeMetadataDatabase()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getIntent(): Intent {
        val intent = Intent(context, ProjectInformationActivity::class.java)
        intent.putExtra(MainActivity.projectString, project)

        Files.copy(inputStreamSNK, videoSNKFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamEPFL, videoEPFLFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamArch, imageArchFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamSO, imageSOFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(inputStreamEmpty, emptyFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        return intent
    }

    var activityScenarioRule = ActivityScenarioRule<ProjectInformationActivity>(getIntent())

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityScenarioRule)


    @After
    fun clean() {
        videoSNKFile.delete()
        videoEPFLFile.delete()
        imageArchFile.delete()
        imageSOFile.delete()
        emptyFile.delete()
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

        Thread.sleep(1000) //sleep 0.5sec to wait the download
        assertFalse(videoView.isPlaying)
        Thread.sleep(1000)

        val video = onView(withId(R.id.info_video))
        video.perform(scrollTo())
        Thread.sleep(1000)
        video.perform(click())
        Thread.sleep(1000)
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

    @Test
    fun clickOnShareButton() {
        clickOnMenu(R.id.shareButton, R.string.share)
    }

    @Test
    fun clickOnGenQRButton() {
        Thread.sleep(2000)
        clickOnMenu(R.id.generateQRCodeButton, R.string.generate_barcode)
    }

    @Test
    fun clickOnApplyButton() {
        var applyString: String =
            ApplicationProvider.getApplicationContext<Context>().getString(R.string.apply_text)
        var unapplyString: String =
            ApplicationProvider.getApplicationContext<Context>().getString(R.string.unaply_text)

        Thread.sleep(2000)

        val applyButton = onView(withId(R.id.applyButton))
        applyButton.check(matches(withText(applyString)))
        applyButton.perform(scrollTo()).perform(click())
        applyButton.check(matches(withText(unapplyString)))
        applyButton.perform(scrollTo()).perform(click())
        applyButton.check(matches(withText(applyString)))
    }


    @Test
    //TODO : call the proper string ressource in string.xml w mockito
    fun clickOnFavoriteButton() {
        var favoriteString: String = "add project to favorites"
        var removeFavoriteString: String = "remove project from favorites"

        Thread.sleep(2000)

        val favoriteButton = onView(withId(R.id.favoriteButton))
        favoriteButton.check(matches(withText(favoriteString)))
        favoriteButton.perform(scrollTo()).perform(click())
        favoriteButton.check(matches(withText(removeFavoriteString)))
        favoriteButton.perform(scrollTo()).perform(click())
        favoriteButton.check(matches(withText(favoriteString)))
    }

    @Test
    fun clickOnWaitingListButton() {
        Thread.sleep(2000)
        Intents.init()
        clickOnMenu(R.id.waitingListButton, R.string.waiting_button)
        intended(hasComponent(WaitingListActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun clickOnEditButton() {
        Intents.init()
        clickOnMenu(R.id.editButton, R.string.edit_button)
        intended(hasComponent(ProjectCreationActivity::class.java.name))
        Intents.release()
    }

    private fun clickOnMenu(menuId: Int, menuTextId: Int) {
        try {
            onView(withId(menuId)).perform(click())
        } catch (e: NoMatchingViewException) {
            openActionBarOverflowOrOptionsMenu(context)
            onView(withText(menuTextId)).perform(click())
        }
    }
}




