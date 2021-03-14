package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity

@RunWith(AndroidJUnit4::class)
class ListOfProjectsActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<ProjectsListActivity> =
        ActivityScenarioRule(ProjectsListActivity::class.java)

    @Test
    fun userCanScroll() {
        onView(withId(R.id.recycler_view))
            .perform(ViewActions.swipeUp())

    }

}