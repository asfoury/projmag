package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class ProfilePageTestEdit {
    @get:Rule
    var activityRule: ActivityScenarioRule<ProfilePageActivity> =
        ActivityScenarioRule(ProfilePageActivity::class.java)

    @Test
    fun userCanPressOnProject() {
        Espresso.onView(ViewMatchers.withId(R.id.button_edit_profile)).perform(click()
            )

    }
}