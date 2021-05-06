package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.PreferencesActivity
import com.sdp13epfl2021.projmag.activities.ProfilePageActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.prefs.Preferences

@RunWith(AndroidJUnit4::class)
class PreferencesActivityTests {

    @get:Rule
    var activityRule: ActivityScenarioRule<PreferencesActivity> =
        ActivityScenarioRule(PreferencesActivity::class.java)


    @Test
    fun activityDoNotCrash() {
        onView(withId(R.id.filter_bachelor)).perform(click())
        onView(withId(R.id.filter_master)).perform(click())
        onView(withId(R.id.filter_applied)).perform(click())
        onView(withId(R.id.preferences_layout_submit)).perform(click())
    }
}