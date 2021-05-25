package com.sdp13epfl2021.projmag

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sdp13epfl2021.projmag.activities.OrientationActivity
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrientationActivityTest {
    @get:Rule
    var mActivityTestRule = ActivityTestRule(OrientationActivity::class.java)

    @Test
    fun testDefaultPosition() {
        mActivityTestRule.activity.computeOrientationAngles()
        val orientationAngle = mActivityTestRule.activity.getOrientationAngles()
        assertEquals(-0.0, orientationAngle[0].toDouble())
        assertEquals(-0.0, orientationAngle[2].toDouble())
        assertEquals(-1.487892746925354, orientationAngle[1].toDouble())

    }
}
