package com.sdp13epfl2021.projmag

import android.content.Context
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.CommentsActivity
import com.sdp13epfl2021.projmag.activities.PreferencesActivity
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import com.sdp13epfl2021.projmag.database.di.CommentsDatabaseModule
import com.sdp13epfl2021.projmag.database.di.UserIdModule
import com.sdp13epfl2021.projmag.database.di.UserdataDatabaseModule
import com.sdp13epfl2021.projmag.database.interfaces.CommentsDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.Message
import com.sdp13epfl2021.projmag.model.ProjectFilter
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assume
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Named

@UninstallModules(UserdataDatabaseModule::class,CommentsDatabaseModule::class, UserIdModule::class)
@HiltAndroidTest
class CommentsUITest {


    var activityRule: ActivityScenarioRule<CommentsActivity> =
        ActivityScenarioRule(CommentsActivity::class.java)

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityRule)


    private val messages : List<Message> = listOf(
        Message("test","test", 100000),
        Message("test","test", 100000),
        Message("test","test", 100000),
        Message("test","test", 100000),
        Message("test","test", 100000),
        Message("test","test", 100000),
    )

    @BindValue
    val userDB: UserdataDatabase = Mockito.mock(UserdataDatabase::class.java).apply {

    }



    @BindValue
    val commentDB: CommentsDatabase = Mockito.mock(CommentsDatabase::class.java).apply {
        Mockito.`when`(this.getCommentsOfProject(anyObject(), anyObject(), anyObject())).then {
            @Suppress("UNCHECKED_CAST") val f = it.arguments[1] as Function1<List<Message>, Unit>
            f(messages)
        }

        Mockito.`when`(this.addListener(anyObject(), anyObject())).then {
        }


    }

    @BindValue
    @Named("currentUserId")
    val userId: String = "user-id-mock"


    @Test
    fun userCanScroll() {
        onView(withId(R.id.recycler_view_comments))
            .perform(ViewActions.swipeUp())
    }

    @Test
    fun userCanClickOnCommentsButton() {
        onView(withId(R.id.comments_edit_text))
            .perform(replaceText("Hello! this is a question"))
        onView(withId(R.id.comments_edit_text)).check(matches((withText("Hello! this is a question"))));
    }

    @Test
    fun userCanClickOnSend() {
        onView(withId(R.id.comments_edit_text))
            .perform(replaceText("Hello! this is a question"))
        onView(withId(R.id.comments_edit_text)).check(matches((withText("Hello! this is a question"))));
        onView(withId(R.id.comments_send_button))
            .perform(click())
    }


}