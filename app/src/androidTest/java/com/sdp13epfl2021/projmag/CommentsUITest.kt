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
import com.sdp13epfl2021.projmag.activities.CommentsActivity
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import org.junit.Assume
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentsUITest {

    @get:Rule
    var activityRule: ActivityScenarioRule<CommentsActivity> =
        ActivityScenarioRule(CommentsActivity::class.java)

    @Test
    fun userCanScroll() {
        onView(withId(R.id.recycler_view_comments))
            .perform(ViewActions.swipeUp())
    }

    @Test
    fun userCanClickOnCommentsButton() {
        onView(withId(R.id.comments_edit_text))
            .perform(replaceText("Hello! this is a question"))
    }

    @Test
    fun userCanClickOnSend() {
        onView(withId(R.id.comments_edit_text))
            .perform(replaceText("Hello! this is a question"))
        onView(withId(R.id.comments_send_button))
            .perform(click())
    }


}