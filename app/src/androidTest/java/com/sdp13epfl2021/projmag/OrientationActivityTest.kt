package com.sdp13epfl2021.projmag

import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

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
