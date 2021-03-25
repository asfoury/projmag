package com.sdp13epfl2021.projmag


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
    var mActivityTestRule = ActivityScenarioRule(Form::class.java)

    @Test
    fun formTest() {
        val appCompatEditText = onView(
                allOf(withId(R.id.form_edit_text_project_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("Nicolas"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.form_edit_text_laboratory),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("computer"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
                allOf(withId(R.id.form_edit_text_project_name), withText("Nicolas"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("Manager"))

        val appCompatEditText4 = onView(
                allOf(withId(R.id.form_edit_text_project_name), withText("Manager"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()))
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.form_edit_text_teacher),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("Nicolas"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
                allOf(withId(R.id.form_edit_text_project_TA),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText6.perform(replaceText("mike"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
                allOf(withId(R.id.form_edit_text_project_TA), withText("mike"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatEditText7.perform(pressImeActionButton())

        val appCompatEditText8 = onView(
                allOf(withId(R.id.form_nb_of_participant),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText8.perform(replaceText("2"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
                allOf(withId(R.id.form_nb_of_participant), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatEditText9.perform(pressImeActionButton())

        val materialCheckBox = onView(
                allOf(withId(R.id.form_check_box_SP), withText("Semester project"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()))
        materialCheckBox.perform(click())

        val appCompatEditText10 = onView(
                allOf(withId(R.id.form_project_description),
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
