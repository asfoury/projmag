package com.sdp13epfl2021.projmag


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProfilePageActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfilePageActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ProfilePageActivity::class.java)

    @Test
    fun testSaveProfile() {
        Thread.sleep(4000)
        onView(withId(R.id.buttonSubChangeProfil))
            .perform(click())
    }

    @Test
    fun profilePageActivityTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.email_profile),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("mik@e"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.email_profile), withText("mik@e"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.profile_lastname),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("mike"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.profile_firstname),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("miller"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.profile_age),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("22"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.profile_genre),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("male"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.profile_phone_number),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        4
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("7777777"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.profile_sciper),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        4
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText("22222"), closeSoftKeyboard())
    }




    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

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
