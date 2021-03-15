package com.sdp13epfl2021.projmag


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FormTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Form::class.java)

    @Test
    fun formTest() {
        val appCompatEditText = onView(
                allOf(withId(R.id.EditTextProjectName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("Nicolas"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.EditTextLaboratory),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("computer"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.EditTextProjectName), withText("Nicolas"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("Manager"))

        val appCompatEditText4 = onView(
                allOf(withId(R.id.EditTextProjectName), withText("Manager"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.EditTextTeacher),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("Nicolas"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
                allOf(withId(R.id.EditTextProjectTA),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText6.perform(replaceText("mike"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
                allOf(withId(R.id.EditTextProjectTA), withText("mike"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText7.perform(pressImeActionButton())

        val appCompatEditText8 = onView(
                allOf(withId(R.id.nbOfParticipant),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText8.perform(replaceText("2"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
                allOf(withId(R.id.nbOfParticipant), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText9.perform(pressImeActionButton())

        val materialCheckBox = onView(
                allOf(withId(R.id.CheckBoxSP), withText("Semester project"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()))
        materialCheckBox.perform(click())

        val appCompatEditText10 = onView(
                allOf(withId(R.id.projectDescriptionForm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()))
        appCompatEditText10.perform(replaceText("Do nothing\n"), closeSoftKeyboard())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}