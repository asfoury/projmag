package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.ProfilePageActivity
import com.sdp13epfl2021.projmag.database.di.UserIdModule
import com.sdp13epfl2021.projmag.database.di.UserdataDatabaseModule
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.Gender
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import com.sdp13epfl2021.projmag.model.Role
import com.sdp13epfl2021.projmag.model.Success
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito
import javax.inject.Named

@UninstallModules(UserdataDatabaseModule::class, UserIdModule::class)
@HiltAndroidTest
class ProfilePageActivityTest {

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(ActivityScenarioRule(ProfilePageActivity::class.java))

    @BindValue
    @Named("currentUserId")
    val userID: String = "uid"

    val dummyProfile = (ImmutableProfile.build(
        lastName = "Foo",
        firstName = "Bar",
        42,
        Gender.MALE,
        0,
        "00000",
        Role.STUDENT // only student role for now
    ) as Success<ImmutableProfile>).value

    val otherDummyProfile = (ImmutableProfile.build(
        lastName = "first",
        firstName = "last",
        22,
        Gender.FEMALE,
        0,
        "0101010100",
        Role.STUDENT // only student role for now
    ) as Success<ImmutableProfile>).value

    @BindValue
    val userDB: UserdataDatabase =
        Mockito.mock(UserdataDatabase::class.java).apply {
            Mockito.`when`(this.getProfile(anyObject(), anyObject(), anyObject()))
                .then {
                    assertEquals(userID, it.arguments[0])
                    @Suppress("UNCHECKED_CAST")
                    val success = it.arguments[1] as Function1<ImmutableProfile, Unit>
                    success(dummyProfile)
                }
        }

    @Test
    fun loadValuesAreDisplayedCorrectly() {
        Thread.sleep(1000) // wait UI to UPDATE
        onView(withId(R.id.profile_lastname)).perform(scrollTo())
            .check(matches(withText(dummyProfile.lastName)))

        onView(withId(R.id.profile_firstname)).perform(scrollTo())
            .check(matches(withText(dummyProfile.firstName)))

        onView(withId(R.id.profile_age)).perform(scrollTo())
            .check(matches(withText(dummyProfile.age.toString())))

        onView(withId(R.id.profile_genre)).perform(scrollTo())
            .check(matches(withText(dummyProfile.gender.toString())))
        onView(withId(R.id.profile_phone_number)).perform(scrollTo())
            .check(matches(withText(dummyProfile.phoneNumber)))
        onView(withId(R.id.profile_sciper))
            .check(matches(withText(dummyProfile.sciper.toString())))
    }

    @Test
    fun profileIsSubmittedCorrectly() {
        Thread.sleep(1000) // wait UI to UPDATE

        Mockito.`when`(userDB.uploadProfile(anyObject(), anyObject(), anyObject())).then {
            val profile = it.arguments[0] as ImmutableProfile
            assertEquals(otherDummyProfile, profile)
        }

        onView(withId(R.id.profile_lastname)).perform(
                scrollTo(),
                replaceText(otherDummyProfile.lastName)
        )
        onView(withId(R.id.profile_firstname)).perform(
            scrollTo(),
            replaceText(otherDummyProfile.firstName)
        )
        onView(withId(R.id.profile_age)).perform(
            scrollTo(),
            replaceText(otherDummyProfile.age.toString())
        )
        onView(withId(R.id.profile_genre)).perform(
            scrollTo(),
            replaceText(otherDummyProfile.gender.name)
        )
        onView(withId(R.id.profile_phone_number)).perform(
            scrollTo(),
            replaceText(otherDummyProfile.phoneNumber),
            closeSoftKeyboard()
        )

        onView(withId(R.id.button_sub_change_profile)).perform(click())
    }

}
