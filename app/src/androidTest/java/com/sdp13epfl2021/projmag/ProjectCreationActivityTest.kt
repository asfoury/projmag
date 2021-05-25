package com.sdp13epfl2021.projmag

import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.FormHelper
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity
import com.sdp13epfl2021.projmag.database.di.*
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Success
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito
import java.io.File
import javax.inject.Named

@HiltAndroidTest
@UninstallModules(
        ProjectDatabaseModule::class,
        FileDatabaseModule::class,
        MetadataDatabaseModule::class,
        CandidatureDatabaseModule::class,
        UserIdModule::class
)
class ProjectCreationActivityTest {

    private var activityRule: ActivityScenarioRule<ProjectCreationActivity> =
            ActivityScenarioRule(ProjectCreationActivity::class.java)

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
            .around(activityRule)


    private val description = """
                Hannah Glasse (née Allgood; March 1708 – 1 September 1770) was an English cookery writer of the 18th century. Her first cookery book, The Art of Cookery Made Plain and Easy, published in 1747, became the best-selling recipe book that century. 
                It was reprinted within its first year of publication, appeared in 20 editions in the 18th century, and continued to be published until well into the 19th century. 
                She later wrote The Servants' Directory (1760) and The Compleat Confectioner, which was probably published in 1760; neither book was as commercially successful as her first.
                Glasse was born in London to a Northumberland landowner and his mistress. After the relationship ended, Glasse was brought up in her father's family. 
                When she was 16 she eloped with a 30-year-old Irish subaltern then on half-pay and lived in Essex, working on the estate of the Earls of Donegall. The couple struggled financially and, with the aim of raising money, 
                Glasse wrote The Art of Cookery. She copied extensively from other cookery books, around a third of the recipes having been published elsewhere. 
                Among her original recipes are the first known curry recipe written in English, as well as three recipes for pilau, an early reference to vanilla in English cuisine, 
                the first recorded use of jelly in trifle, and an early recipe for ice cream. She was also the first to use the term "Yorkshire pudding" in print.
                Glasse became a dressmaker in Covent Garden—where her clients included Princess Augusta, the Princess of Wales—but she ran up excessive debts. She was imprisoned for bankruptcy and was forced to sell the copyright of The Art of Cookery. 
                Much of Glasse's later life is unrecorded; information about her identity was lost until uncovered in 1938 by the historian Madeleine Hope Dodds.
            """.trimIndent()

    private val project = (ImmutableProject.build(
            id = "",
            authorId = "authorId",
            name = "Project1",
            lab = "lab",
            teacher = "teacher",
            TA = "TA",
            nbParticipant = 1,
            assigned = listOf("student"),
            masterProject = true,
            bachelorProject = true,
            tags = listOf(),
            isTaken = false,
            description = description,
            videoURI = listOf()
    ) as Success<ImmutableProject>).value


    @BindValue
    val projectDB: ProjectDatabase = Mockito.mock(ProjectDatabase::class.java)

    @BindValue
    val fileDB: FileDatabase = Mockito.mock(FileDatabase::class.java)

    @BindValue
    val metadataDB: MetadataDatabase = Mockito.mock(MetadataDatabase::class.java)

    @BindValue
    val candidatureDB: CandidatureDatabase = Mockito.mock(CandidatureDatabase::class.java)

    @BindValue
    @Named("currentUserId")
    val userID: String = "uid"


    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun writeAProjectToSubmit() {
        Mockito.`when`(projectDB.pushProject(anyObject(), anyObject(), anyObject())).then {
            val p = it.arguments[0] as ImmutableProject
            assertEquals(project, p)
        }

        Mockito.`when`(candidatureDB.addListener(anyObject(), anyObject())).then {}

        // need to swipe down to make sure textFields are visible when running tests
        onView(withId(R.id.project_submission_scrollview))
            .perform(swipeDown())

        Thread.sleep(2000)

        onView(withId(R.id.form_edit_text_laboratory))
                .perform(replaceText(project.lab))

        onView(withId(R.id.form_edit_text_project_name))
                .perform(replaceText(project.name))

        onView(withId(R.id.form_edit_text_teacher))
                .perform(replaceText(project.teacher))

        onView(withId(R.id.form_edit_text_project_TA))
                .perform(replaceText(project.TA))

        onView(withId(R.id.form_nb_of_participant))
                .perform(replaceText(project.nbParticipant.toString()))

        onView(withId(R.id.form_check_box_MP))
                .perform(click())

        onView(withId(R.id.form_check_box_SP))
                .perform(click())

        onView(withId(R.id.form_project_description))
                .perform(
                        replaceText(
                                project.description
                        )
                )
    }

    @Test
    fun testFormHelperFunctions() {
        val vidButton = Button(instrumentationContext)
        val subButton = Button(instrumentationContext)
        val mediaController = MediaController(instrumentationContext)
        val fakeStringPath = Uri.fromFile(File("test"))
        val vidView = VideoView(instrumentationContext)

        FormHelper.playVideoFromLocalPath(
                vidButton,
                subButton,
                vidView,
                mediaController,
                fakeStringPath
        )
        assertTrue(vidButton.isEnabled)
        assertTrue(subButton.isEnabled)
        assertTrue(vidButton.hasOnClickListeners())
        assertTrue(!vidView.isPlaying)
    }
}
