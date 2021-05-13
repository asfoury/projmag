package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.ProfilePageActivity
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import dagger.hilt.android.testing.BindValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ProfilePageActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<ProfilePageActivity> =
        ActivityScenarioRule(ProfilePageActivity::class.java)

    @Inject
    @Named("currentUserId")
    lateinit var userID: String


    @BindValue
    val profilePageActivityTest = Mockito.mock(UserdataDatabase::class.java).apply {
        Mockito.`when`(this.getProfile(userID, anyObject(), anyObject()))
            .then {}
    }

    @Test
    fun testChangingValues() {
        onView(withId(R.id.profile_lastname)).perform(scrollTo(), clearText(), typeText("lastName"))
        onView(withId(R.id.profile_firstname)).perform(
            scrollTo(),
            clearText(),
            typeText("firstName")
        )
        onView(withId(R.id.profile_age)).perform(scrollTo(), clearText(), typeText("21"))
        onView(withId(R.id.profile_genre)).perform(scrollTo(), clearText(), typeText("Male"))
        onView(withId(R.id.profile_phone_number)).perform(
            scrollTo(),
            clearText(),
            typeText("0101010100")
        )
    }

}
