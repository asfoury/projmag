package com.sdp13epfl2021.projmag


import android.provider.Settings
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.activities.SignInActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


@HiltAndroidTest
class SignInActivityTest {
    val scenarioRule = ActivityScenarioRule(SignInActivity::class.java)

    @get:Rule
    val testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(scenarioRule)

    @Test
    fun testSignInButton() {
        Thread.sleep(4000) // wait for pop-up
        onView(withId(android.R.id.button2)).perform(click())
        onView(withId(R.id.signInButton)).perform(click())
    }

    @Test
    fun testOpenCaptionSettings() {
        Intents.init()
        Thread.sleep(4000) // wait for pop-up
        onView(withId(android.R.id.button1)).perform(click())
        Intents.intended(IntentMatchers.hasAction(Settings.ACTION_CAPTIONING_SETTINGS))

        Intents.release()
    }
}

