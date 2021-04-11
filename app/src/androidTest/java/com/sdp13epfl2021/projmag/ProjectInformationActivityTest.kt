package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import org.junit.Rule
import org.junit.Test

class ProjectInformationActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<ProjectsListActivity> =
        ActivityScenarioRule(ProjectsListActivity::class.java)

    @Test
    fun userCanPressOnProject() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ItemViewHolder>(
                0,
                MyViewAction.clickChildViewWithId(R.id.project_title)
            )
        )
    }


    @Test
    fun userCanPressOnProjectAndGoBackUsingBackButton() {
        // press on first project
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ItemViewHolder>(
                0,
                MyViewAction.clickChildViewWithId(R.id.project_title)
            )
        )
        // go back to list of project
        pressBack()
        // press on second project
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ItemViewHolder>(
                1,
                MyViewAction.clickChildViewWithId(R.id.project_title)
            )
        )
        // go back to list of projects
        pressBack()
    }
}
