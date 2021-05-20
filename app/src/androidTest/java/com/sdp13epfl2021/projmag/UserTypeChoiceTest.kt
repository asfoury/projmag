package com.sdp13epfl2021.projmag


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.UserTypeChoice
import com.sdp13epfl2021.projmag.database.di.UserdataDatabaseModule
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito

@UninstallModules(UserdataDatabaseModule::class)
@HiltAndroidTest
class UserTypeChoiceTest {
    private val activityScenario = ActivityScenarioRule(UserTypeChoice::class.java)

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(activityScenario)

    @BindValue
    val mUserDB: UserdataDatabase = Mockito.mock(UserdataDatabase::class.java).also {
        // this is to avoid using the real DB when starting the next activity
        Mockito.`when`(it.getProfile(anyObject(), anyObject(), anyObject())).then {}
    }

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
