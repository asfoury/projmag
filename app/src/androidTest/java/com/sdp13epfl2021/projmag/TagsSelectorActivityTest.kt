package com.sdp13epfl2021.projmag

import android.content.Intent
import android.nfc.Tag
import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.TagsSelectorActivity
import com.sdp13epfl2021.projmag.adapter.TagAdapter

class TagsSelectorActivityTest {
    val thirdItem = 3

    private lateinit var intent : Intent

    @get:Rule
    var activityRule: ActivityScenarioRule<TagsSelectorActivity>
    = ActivityScenarioRule(TagsSelectorActivity::class.java)


    //recycler view comes into view
    @Test
    fun test_isTagListVisible() {
        onView(withId(R.id.recycler_tag_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_selectListItem_IsDetailOfItemVisible() {

        //perform a click action on an element of a tag view
        onView(withId(R.id.recycler_tag_view)).perform(actionOnItemAtPosition<TagAdapter.TagViewHolder>(3, click()))

        //scroll to an element of a tag view
        onView(withId(R.id.recycler_tag_view)).perform(RecyclerViewActions.scrollToPosition<TagAdapter.TagViewHolder>(4))


    }
}