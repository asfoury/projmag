package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.SignInActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    @get:Rule
    var testRule = ActivityScenarioRule<SignInActivity>(SignInActivity::class.java)

    @Test
    fun testSignInButton() {
        Thread.sleep(1000) // wait for pop-up
        onView(withId(android.R.id.button2)).perform(click())
        onView(withId(R.id.signInButton)).perform(click())
    }

    @Test
    fun testOpenCaptionSettings() {
        Thread.sleep(1000) // wait for pop-up
        onView(withId(android.R.id.button1)).perform(click())
    }
}

