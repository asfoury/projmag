package com.sdp13epfl2021.projmag

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.activities.SectionSelectionActivity
import com.sdp13epfl2021.projmag.activities.TagsSelectorActivity
import com.sdp13epfl2021.projmag.adapter.TagAdapter
import org.junit.Rule
import org.junit.Test

class SectionSelectorActivityTest {
    val thirdItem = 3

    private lateinit var intent : Intent

    @get:Rule
    var activityRule: ActivityScenarioRule<SectionSelectionActivity>
            = ActivityScenarioRule(SectionSelectionActivity::class.java)


    //recycler view comes into view
    @Test
    fun test_isSectionListVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.recycler_section_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_selectListItem_IsDetailOfItemVisible() {

        //perform a click action on an element of a tag view
        Espresso.onView(ViewMatchers.withId(R.id.recycler_section_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<TagAdapter.TagViewHolder>(
                3,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.recycler_section_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<TagAdapter.TagViewHolder>(
                3,
                ViewActions.longClick()
            )
        )
        //scroll to an element of a tag view
        Espresso.onView(ViewMatchers.withId(R.id.recycler_section_view))
            .perform(RecyclerViewActions.scrollToPosition<TagAdapter.TagViewHolder>(4))


    }
}