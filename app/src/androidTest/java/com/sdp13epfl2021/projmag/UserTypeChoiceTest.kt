package com.sdp13epfl2021.projmag


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.UserTypeChoice
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class UserTypeChoiceTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(UserTypeChoice::class.java)

    @Test
    fun userProf() {
        onView(withId(R.id.radioProfessorType)).perform(click())
        assertTrue(UserTypeChoice.isProfessor)
    }

    @Test
    fun userStudent() {
        onView(withId(R.id.radioStudentType)).perform(click())
        assertFalse(UserTypeChoice.isProfessor)
    }

    @Test
    fun userPHD() {
        onView(withId(R.id.radioPHDType)).perform(click())
        assertTrue(UserTypeChoice.isProfessor)
    }


}
