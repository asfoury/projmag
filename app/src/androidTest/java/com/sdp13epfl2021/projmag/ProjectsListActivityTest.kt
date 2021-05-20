package com.sdp13epfl2021.projmag

import android.content.Context
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import com.sdp13epfl2021.projmag.database.di.ProjectDatabaseModule
import com.sdp13epfl2021.projmag.database.di.UserdataDatabaseModule
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ProjectFilter
import com.sdp13epfl2021.projmag.model.Success
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assume
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito

@UninstallModules(ProjectDatabaseModule::class, UserdataDatabaseModule::class)
@HiltAndroidTest
class ProjectsListActivityTest {

    private val userIsAProfessor: Boolean = true

    private val project1 = (ImmutableProject.build(
        id = "id1",
        authorId = "authorId",
        name = "Project1",
        lab = "lab",
        teacher = "teacher",
        TA = "TA",
        nbParticipant = 1,
        assigned = listOf("student"),
        masterProject = true,
        bachelorProject = true,
        tags = listOf("tag"),
        isTaken = true,
        description = "description",
        videoURI = listOf("uri")
    ) as Success<ImmutableProject>).value

    private val project2 = (ImmutableProject.build(
        id = "id2",
        authorId = "authorId",
        name = "Project2",
        lab = "lab",
        teacher = "teacher",
        TA = "TA",
        nbParticipant = 1,
        assigned = listOf(),
        masterProject = false,
        bachelorProject = false,
        tags = listOf("tag"),
        isTaken = false,
        description = "description",
        videoURI = listOf("uri")
    ) as Success<ImmutableProject>).value

    private val listOfProjects: List<ImmutableProject> = listOf(
        project1,
        project2
    )

    private val pref = ProjectFilter(master = true)

    // -----------------------------------------------------------

    private val activityRule: ActivityScenarioRule<ProjectsListActivity> =
        ActivityScenarioRule(ProjectsListActivity::class.java)

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityRule)


    @BindValue
    val projectDB: ProjectDatabase = Mockito.mock(ProjectDatabase::class.java).also { db ->
        Mockito.`when`(db.getAllProjects(anyObject(), anyObject())).then {
            @Suppress("UNCHECKED_CAST")
            val onSuccess = it.arguments[0] as Function1<List<ImmutableProject>, Unit>
            onSuccess(listOfProjects)
        }

        Mockito.`when`(db.addProjectsChangeListener(anyObject())).then {
            /* Does nothing for now */
        }
    }

    @BindValue
    val userDB: UserdataDatabase = Mockito.mock(UserdataDatabase::class.java).also { db ->
        Mockito.`when`(db.getListOfAppliedToProjects(anyObject(), anyObject())).then { im ->
            @Suppress("UNCHECKED_CAST")
            val onSuccess = im.arguments[0] as Function1<List<ProjectId>, Unit>
            listOfProjects.firstOrNull()?.let { onSuccess(listOf(it.id)) }
        }

        Mockito.`when`(db.getListOfFavoriteProjects(anyObject(), anyObject())).then { im ->
            @Suppress("UNCHECKED_CAST")
            val onSuccess = im.arguments[0] as Function1<List<ProjectId>, Unit>
            listOfProjects.firstOrNull()?.let { onSuccess(listOf(it.id)) }
        }

        Mockito.`when`(db.getPreferences(anyObject(), anyObject())).then { im ->
            @Suppress("UNCHECKED_CAST")
            val onSuccess = im.arguments[0] as Function1<ProjectFilter?, Unit>
            onSuccess(pref)
        }
    }


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
        search.perform(click())
        activityRule.scenario.onActivity { projectListActivity ->
            val dataset = projectListActivity.getItemAdapter().dataset
            var taken = false
            for (project in dataset) {
                if (taken) assert(project.isTaken)
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

    @Test
    fun clearFilterDoNotCrash() {
        onView(withId(R.id.filterButton)).perform(click())
        val clear = ApplicationProvider.getApplicationContext<Context>().getString(R.string.clear)
        onView(withText(clear)).perform(click())
    }

    @Test
    fun cancelFilterDialogDoNotCrash() {
        onView(withId(R.id.filterButton)).perform(click())
        val cancel = ApplicationProvider.getApplicationContext<Context>().getString(R.string.cancel)
        onView(withText(cancel)).perform(click())
    }

    @Test
    fun filterWorks() {
        onView(withId(R.id.filterButton)).perform(click())
        val ok = ApplicationProvider.getApplicationContext<Context>().getString(R.string.ok)
        onView(withId(R.id.filter_bachelor)).perform(click())
        onView(withId(R.id.filter_master)).perform(click())
        onView(withText(ok)).perform(click())
        Thread.sleep(2000)
        onView(withText("Project1")).check(matches(isDisplayed()))
        onView(withText("Project2")).check(doesNotExist())
    }
}