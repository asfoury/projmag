package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    @get:Rule
    var testRule = ActivityScenarioRule<SignInActivity>(SignInActivity::class.java)

    @Test
    fun testSignInButton() {
        Espresso.onView(ViewMatchers.withId(R.id.signInButton)).perform(ViewActions.click())
    }
}

