package com.sdp13epfl2021.projmag

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
        search.perform(ViewActions.click())
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            val recyclerView = projectListActivity.getRecyclerView()
            for (project in dataset) {
                val i = dataset.indexOf(project)
                if (project.isTaken) {
                    assert(recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha == 0.5f)
                } else {
                    assert(recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha == 1f)
                }
            }
        }

        val searchText = onView(isAssignableFrom(AutoCompleteTextView::class.java))
        searchText.perform(ViewActions.typeText("d"))
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            val recyclerView = projectListActivity.getRecyclerView()
            for (project in dataset) {
                val i = dataset.indexOf(project)
                if (project.isTaken) {
                    assert(recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha == 0.5f)
                } else {
                    assert(recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha == 1f)
                }
            }
        }
    }

    @Test
    fun profileButtonOpensSignActivity() {
        onView(withId(R.id.profileButton)).perform(click())

    }

}