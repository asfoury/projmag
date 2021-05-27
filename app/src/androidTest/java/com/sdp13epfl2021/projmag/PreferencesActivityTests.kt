package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.activities.PreferencesActivity
import com.sdp13epfl2021.projmag.activities.UserTypeChoice
import com.sdp13epfl2021.projmag.database.di.UserdataDatabaseModule
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.ProjectFilter
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito

@UninstallModules(UserdataDatabaseModule::class)
@HiltAndroidTest
class PreferencesActivityTests {

    private val filterTrue = ProjectFilter(
        bachelor = true,
        master = true,
        applied = true,
        favorite = true,
        own = true
    )

    private val filterFalse = ProjectFilter(
        bachelor = false,
        master = false,
        applied = false
    )

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(ActivityScenarioRule(PreferencesActivity::class.java))

    @BindValue
    val userDB: UserdataDatabase = Mockito.mock(UserdataDatabase::class.java).apply {
        Mockito.`when`(this.getPreferences(anyObject(), anyObject())).then {
            @Suppress("UNCHECKED_CAST")
            val l = it.arguments[0] as Function1<ProjectFilter?, Unit>
            l.invoke(filterTrue)
        }
    }


    @Test
    fun preferencesAreLoadedCorrectly() {
        UserTypeChoice.isProfessor = true
        Mockito.`when`(userDB.pushPreferences(anyObject(), anyObject(), anyObject())).then {
            val pf = it.arguments[0] as ProjectFilter
            assertThat(pf, `is`(filterTrue))
        }
        Thread.sleep(5000)
        onView(withId(R.id.filter_bachelor)).check(matches(isChecked()))
        onView(withId(R.id.filter_master)).check(matches(isChecked()))
        onView(withId(R.id.filter_favorites)).check(matches(isChecked()))

        // is professor
        onView(withId(R.id.filter_own)).check(matches(isChecked()))

        onView(withId(R.id.preferences_layout_submit)).perform(click())
    }

    @Test
    fun preferencesCanBeModifiedCorrectly() {
        UserTypeChoice.isProfessor = true

        val outFilter = ProjectFilter(
            bachelor = false, master = false, applied = true, favorite = false, own = false
        )
        Mockito.`when`(userDB.pushPreferences(anyObject(), anyObject(), anyObject())).then {
            val pf = it.arguments[0] as ProjectFilter
            assertThat(pf, `is`(outFilter))
        }

        Thread.sleep(5000)

        onView(withId(R.id.filter_bachelor)).perform(click())
        onView(withId(R.id.filter_master)).perform(click())
        onView(withId(R.id.filter_favorites)).perform(click())

        // is professor
        onView(withId(R.id.filter_own)).perform(click())

        onView(withId(R.id.preferences_layout_submit)).perform(click())
    }
}