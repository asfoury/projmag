package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class PreferencesActivityTests {
    @Test
    fun activityDoNotCrash() {
        onView(withId(R.id.filter_bachelor)).perform(click())
        onView(withId(R.id.filter_master)).perform(click())
        onView(withId(R.id.filter_applied)).perform(click())
        onView(withId(R.id.preferences_layout_submit)).perform(click())
    }
}