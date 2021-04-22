package com.sdp13epfl2021.projmag

import android.view.View
import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import org.junit.Assume
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListOfProjectsActivityTest {

    val userIsAProfessor: Boolean = true

    @get:Rule
    var activityRule: ActivityScenarioRule<ProjectsListActivity> =
        ActivityScenarioRule(ProjectsListActivity::class.java)

    @Test
    fun userCanScroll() {
        onView(withId(R.id.recycler_view_project))
            .perform(ViewActions.swipeUp())
    }

    @Test
    fun takenProjectsLastWithoutSearch() {
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            var taken = false
            for (project in dataset) {
                if (taken == true) assert(project.isTaken)
                if (project.isTaken) taken = true
            }
        }
    }

    @Test
    fun takenProjectsLastWithSearch() {
        val search = onView(withId(R.id.searchButton))
        search.perform(ViewActions.click())
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            var taken = false
            for (project in dataset) {
                if (taken == true) assert(project.isTaken)
                if (project.isTaken) taken = true
            }
        }

        val searchText = onView(isAssignableFrom(AutoCompleteTextView::class.java))
        searchText.perform(ViewActions.typeText("d"))
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            var taken = false
            for (project in dataset) {
                if (taken == true) assert(project.isTaken)
                if (project.isTaken) taken = true
            }
        }
    }

    @Test
    fun takenProjectsGreyedOut() {
        val search = onView(withId(R.id.searchButton))
        search.perform(click())
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            val recyclerView = projectListActivity.getRecyclerView()
            for (project in dataset) {
                val i = dataset.indexOf(project)
                val iv: View? = recyclerView.findViewHolderForLayoutPosition(i)?.itemView
                if (iv != null) {
                    if (project.isTaken) {
                        assert(iv.alpha == 0.5f)
                    } else {
                        assert(iv.alpha == 1f)
                    }
                }
            }
        }
    }

    @Test
    fun pressProfileButton() {
        onView(withId(R.id.profileButton)).perform(click())
    }

    @Test
    fun professorCanNavigateToTheFormToSubmitAProject() {
        Assume.assumeTrue("The user is a professor", userIsAProfessor)
        onView(withId(R.id.fab))
            .perform(click())
    }

    @Test
    fun userCanPressOnProject() {
        onView(withId(R.id.recycler_view_project)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ProjectViewHolder>(
                0,
                click()
            )
        )
    }


    @Test
    fun userCanPressOnProjectAndGoBackUsingBackButton() {
        // press on first project
        onView(withId(R.id.recycler_view_project)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ProjectViewHolder>(
                0,
                click()
            )
        )
        // go back to list of project
        Espresso.pressBack()
        // press on second project
        onView(withId(R.id.recycler_view_project)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProjectAdapter.ProjectViewHolder>(
                1,
                click()
            )
        )
        // go back to list of projects
        Espresso.pressBack()
    }
}