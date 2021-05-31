package com.sdp13epfl2021.projmag.video

import android.app.Activity.RESULT_OK
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.VIDEO_SUBTITLING_ACTIVITY_RESULT_KEY
import com.sdp13epfl2021.projmag.activities.VideoSubtitlingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class VideoSubtitlingActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(VideoSubtitlingActivity::class.java)


    val subtitlesText = "some text for subtitles"
    val defaultTimestampText = "00:00:00.000"
    val defaultTimestamp = 0

    @Test
    fun timestampsAreDisplayedRightAtStart() {
        onView(withId(R.id.video_subtitling_videoview))
            .perform(ViewActions.swipeDown())
        Thread.sleep(1000)

        onView(withId(R.id.video_subtitling_start_time)).check(matches(withText(defaultTimestampText)))
        onView(withId(R.id.video_subtitling_end_time)).check(matches(withText(defaultTimestampText)))
    }

    @Test
    fun overallCheck() {
        onView(withId(R.id.video_subtitling_videoview))
            .perform(ViewActions.swipeUp())
        Thread.sleep(1000)
        onView(withId(R.id.video_subtitling_subtitle_text))
            .perform(ViewActions.swipeUp())
        Thread.sleep(1000)

        val builder = SubtitleBuilder()
        builder.start = defaultTimestamp
        builder.end = defaultTimestamp
        builder.add(subtitlesText)

        onView(withId(R.id.video_subtitling_set_start_button)).perform(ViewActions.click())
        onView(withId(R.id.video_subtitling_set_end_button)).perform(ViewActions.click())
        onView(withId(R.id.video_subtitling_start_time)).check(matches(withText(defaultTimestampText)))
        onView(withId(R.id.video_subtitling_end_time)).check(matches(withText(defaultTimestampText)))
        onView(withId(R.id.video_subtitling_subtitle_text)).perform(
            ViewActions.replaceText(
                subtitlesText
            )
        )
        onView(withId(R.id.video_subtitling_add)).perform(ViewActions.click())
        onView(withId(R.id.video_subtitling_submit_button)).perform(ViewActions.click())
        assertThat(activityRule.scenario.result.resultCode, Matchers.`is`(RESULT_OK))
        assertThat(
            activityRule.scenario.result.resultData.getStringExtra(
                VIDEO_SUBTITLING_ACTIVITY_RESULT_KEY
            ),
            Matchers.`is`(builder.build())
        )
    }
}