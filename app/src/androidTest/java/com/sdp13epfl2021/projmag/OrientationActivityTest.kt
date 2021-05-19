package com.sdp13epfl2021.projmag

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.sdp13epfl2021.projmag.activities.UserTypeChoice
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import junit.framework.Assert
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
        assertEquals(-1.5707963705062866, orientationAngle[1].toDouble())

    }
}