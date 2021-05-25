package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sdp13epfl2021.projmag.activities.FormHelper
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.database.fake.FakeFileDatabase
import com.sdp13epfl2021.projmag.database.fake.FakeMetadataDatabase
import com.sdp13epfl2021.projmag.database.fake.FakeProjectDatabase
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.io.File

@RunWith(AndroidJUnit4::class)
class ProjectCreationActivityTest {


    val userId = "abc123"
    private val projectDB = FakeProjectDatabase()
    private val fileDB = FakeFileDatabase()
    private val metadataDB = FakeMetadataDatabase()
    private val mockAuth = Mockito.mock(FirebaseAuth::class.java)
    private val mockUser = Mockito.mock(FirebaseUser::class.java)

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getIntent(): Intent {
        Mockito.`when`(mockAuth.currentUser).thenReturn(mockUser)
        Mockito.`when`(mockUser.uid).thenReturn(userId)

        Utils.getInstance(
            context,
            true,
            auth = mockAuth,
            projectDB = projectDB,
            fileDB = fileDB,
            metadataDB = metadataDB
        )
        return Intent(context, ProjectCreationActivity::class.java)
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<ProjectCreationActivity> =
        ActivityScenarioRule(getIntent())

    @After
    fun clean() {
        Utils.getInstance(context, true)
    }

    private val newLab: String = "Lab Name"
    private val newProjectName: String = "Project Name"
    private val newTeacher: String = "Professor Name"
    private val newTA: String = "TA Name"
    private val newNbParticipant = 2
    private val newDescription = """
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

    @Test
    fun writeAProjectToSubmit() {
        // need to swipe down to make sure textFields are visible when running tests
        onView(withId(R.id.project_submission_scrollview))
            .perform(swipeDown())

        Thread.sleep(1000)

        onView(withId(R.id.form_edit_text_laboratory))
            .perform(scrollTo())
            .perform(replaceText(newLab))

        onView(withId(R.id.form_edit_text_project_name))
            .perform(scrollTo())
            .perform(replaceText(newProjectName))

        onView(withId(R.id.form_edit_text_teacher))
            .perform(scrollTo())
            .perform(replaceText(newTeacher))

        onView(withId(R.id.form_edit_text_project_TA))
            .perform(scrollTo())
            .perform(replaceText(newTA))

        onView(withId(R.id.form_nb_of_participant))
            .perform(scrollTo())
            .perform(replaceText(newNbParticipant.toString()))

        onView(withId(R.id.form_check_box_MP))
            .perform(scrollTo())
            .perform(click())

        onView(withId(R.id.form_check_box_SP))
            .perform(scrollTo())
            .perform(click())

        onView(withId(R.id.form_project_description))
            .perform(scrollTo())
            .perform(
                replaceText(newDescription)
            )

        onView(withId(R.id.form_button_sub))
            .perform(scrollTo())
            .perform(click())


        Thread.sleep(2000)
        assertEquals(1, projectDB.projects.size)
        projectDB.projects[0].let {
            assertEquals(newProjectName, it.name)
            assertEquals(newLab, it.lab)
            assertEquals(newTeacher, it.teacher)
            assertEquals(newTA, it.TA)
            assertEquals(newNbParticipant, it.nbParticipant)
            assertEquals(newDescription, it.description)
        }
    }

    @Test
    fun testFormHelperFunctions() {
        val vidButton = Button(context)
        val subButton = Button(context)
        val mediaController = MediaController(context)
        val fakeStringPath = Uri.fromFile(File("test"))
        val vidView = VideoView(context)

        FormHelper.playVideoFromLocalPath(
            vidButton,
            subButton,
            vidView,
            mediaController,
            fakeStringPath
        )
        assert(vidButton.isEnabled)
        assert(subButton.isEnabled)
        assert(vidButton.hasOnClickListeners())
        assert(!vidView.isPlaying)
    }
}
