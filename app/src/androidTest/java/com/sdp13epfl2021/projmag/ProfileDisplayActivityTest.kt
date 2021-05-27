package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.ProfileDisplayActivity
import com.sdp13epfl2021.projmag.model.Gender
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import com.sdp13epfl2021.projmag.model.Role
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileDisplayActivityTest {

    private val profile: ImmutableProfile = ImmutableProfile(
        "lastname1",
        "firstname2",
        20,
        Gender.MALE,
        123456,
        "021 234 56 78",
        Role.STUDENT,
    )

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getIntent(): Intent {
        val intent = Intent(context, ProfileDisplayActivity::class.java)
        intent.putExtra(MainActivity.profile, profile as Parcelable)
        return intent
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<ProfileDisplayActivity> =
        ActivityScenarioRule(getIntent())

    @Test
    fun activityDisplayCorrectlyTheProfile() {
        checkTextForView(R.id.profile_display_firstname, "Firstname : ${profile.firstName}")
        checkTextForView(R.id.profile_display_lastname, "Lastname : ${profile.lastName}")
        checkTextForView(R.id.profile_display_age, "Age : ${profile.age}")
        checkTextForView(R.id.profile_display_gender, "Gender : ${profile.gender}")
        checkTextForView(R.id.profile_display_sciper, "Sciper : ${profile.sciper}")
        checkTextForView(R.id.profile_display_phone, "Phone : ${profile.phoneNumber}")
        checkTextForView(R.id.profile_display_role, "Role : ${profile.role}")
    }

    private fun checkTextForView(viewId: Int, expectedText: String) {
        onView(withId(viewId))
            .perform(scrollTo())
            .check(matches(withText(expectedText)))
    }
}