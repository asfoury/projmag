package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity
import com.sdp13epfl2021.projmag.model.ImmutableProject
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class ProjectEditTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getIntent(): Intent {
        val intent = Intent(context, ProjectCreationActivity::class.java)
        val project = ImmutableProject(
            "a", "b", "c", "d", "e", "f", 4,
            listOf(), false, true, listOf(), false, "a"
        )
        intent.putExtra("edit", project)
        return intent
    }


    var activityScenarioRule = ActivityScenarioRule<ProjectCreationActivity>(getIntent())

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityScenarioRule)


    @Test
    fun changeName() {
        onView(withId(R.id.form_edit_text_project_name)).perform(
            scrollTo(),
            clearText(),
            typeText("new name")
        )
        onView(withId(R.id.form_button_sub)).perform(scrollTo(), click())
    }
}
