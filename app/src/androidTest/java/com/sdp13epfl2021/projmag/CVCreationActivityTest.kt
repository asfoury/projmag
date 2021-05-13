package com.sdp13epfl2021.projmag


import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.CVCreationActivity
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CVCreationActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CVCreationActivity::class.java)

    private fun swipe(id: Int) {
        SystemClock.sleep(1000)
        onView(withId(id)).perform(swipeLeft())
        SystemClock.sleep(1000)
    }

    private val exampleCV = CurriculumVitae(
        "hello",
        listOf(PeriodDescription("period", "location", "description", 2000, 2001)),
        listOf(),
        listOf(Language("language", Language.Level.Basic)),
        listOf(SkillDescription("skill", SkillDescription.SkillLevel.Basic))
    )

    @Test
    fun cVCreationActivityTest() {
        swipe(R.id.cv_intro_title)
        val appCompatEditText = onView(
            allOf(
                withId(R.id.cv_summary_body),
                childAtPosition(
                    allOf(
                        withId(R.id.content),
                        childAtPosition(
                            withClassName(`is`("android.widget.FrameLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText(exampleCV.summary), closeSoftKeyboard())

        swipe(R.id.cv_summary_title)

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.cv_period_name),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(
            replaceText(exampleCV.education.first().name),
            closeSoftKeyboard()
        )

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.cv_period_description),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(
            replaceText(exampleCV.education.first().description),
            closeSoftKeyboard()
        )

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.cv_period_from),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(
            replaceText(exampleCV.education.first().from.toString()),
            closeSoftKeyboard()
        )

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.cv_period_to),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(
            replaceText(exampleCV.education.first().to.toString()),
            closeSoftKeyboard()
        )

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.cv_period_location),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(
            replaceText(exampleCV.education.first().location),
            closeSoftKeyboard()
        )

        val materialButton = onView(
            allOf(
                withId(R.id.cv_period_add), withText("Add"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        swipe(R.id.cv_period_title)
        swipe(R.id.cv_period_title)

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.cv_language_name),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(
            replaceText(exampleCV.languages.first().language),
            closeSoftKeyboard()
        )

        val materialButton2 = onView(
            allOf(
                withId(R.id.cv_language_add), withText("Add"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        swipe(R.id.cv_language_title)

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.cv_skills_name),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText(exampleCV.skills.first().name), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.cv_skills_add), withText("Add"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        swipe(R.id.cv_skills_title)

        val materialButton4 = onView(
            allOf(
                withId(R.id.cv_submission_button), withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )

        val cv = mActivityTestRule.activity.buildCV()

        Assert.assertEquals(exampleCV, cv)

        materialButton4.perform(click())
    }

    @Test
    fun deleteViewPeriodWorks() {
        swipe(R.id.cv_intro_title)
        swipe(R.id.cv_summary_title)
        onView(withId(R.id.cv_period_name)).perform(replaceText("something"), closeSoftKeyboard())
        onView(withId(R.id.cv_period_description)).perform(replaceText("else"), closeSoftKeyboard())
        onView(withId(R.id.cv_period_location)).perform(replaceText("hi"), closeSoftKeyboard())
        onView(withId(R.id.cv_period_from)).perform(replaceText("2000"), closeSoftKeyboard())
        onView(withId(R.id.cv_period_to)).perform(replaceText("2020"), closeSoftKeyboard())
        onView(withId(R.id.cv_period_add)).perform(click())

        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(matches(isDisplayed()))
        onView(withId(R.id.cv_item_cardview_button)).perform(click())
        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(
            doesNotExist()
        )

    }

    @Test
    fun deleteViewLanguageWorks() {
        swipe(R.id.cv_intro_title)
        swipe(R.id.cv_summary_title)
        swipe(R.id.cv_period_title)
        swipe(R.id.cv_period_title)
        onView(withId(R.id.cv_language_name)).perform(replaceText("something"), closeSoftKeyboard())
        onView(withId(R.id.cv_language_add)).perform(click())

        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(matches(isDisplayed()))
        onView(withId(R.id.cv_item_cardview_button)).perform(click())
        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(
            doesNotExist()
        )

    }


    @Test
    fun deleteViewSkillsWorks() {
        swipe(R.id.cv_intro_title)
        swipe(R.id.cv_summary_title)
        swipe(R.id.cv_period_title)
        swipe(R.id.cv_period_title)
        swipe(R.id.cv_language_title)
        onView(withId(R.id.cv_skills_name)).perform(replaceText("something"), closeSoftKeyboard())
        onView(withId(R.id.cv_skills_add)).perform(click())

        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(matches(isDisplayed()))
        onView(withId(R.id.cv_item_cardview_button)).perform(click())
        onView(
            allOf(
                withText(containsString("something")),
                withId(R.id.cv_cardview_textview)
            )
        ).check(
            doesNotExist()
        )

    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
