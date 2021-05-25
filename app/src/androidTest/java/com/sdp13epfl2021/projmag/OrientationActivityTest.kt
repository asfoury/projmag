package com.sdp13epfl2021.projmag

import androidx.test.rule.ActivityTestRule
import com.sdp13epfl2021.projmag.activities.OrientationActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class OrientationActivityTest {
    var mActivityTestRule = ActivityTestRule(OrientationActivity::class.java)

    @get:Rule
    val testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(mActivityTestRule)

    @Test
    fun testDefaultPosition() {
        mActivityTestRule.activity.computeOrientationAngles()
        val orientationAngle = mActivityTestRule.activity.getOrientationAngles()
        assertEquals(-0.0, orientationAngle[0].toDouble())
        assertEquals(-0.0, orientationAngle[2].toDouble())
        assertEquals(-1.487892746925354, orientationAngle[1].toDouble())

    }
}
