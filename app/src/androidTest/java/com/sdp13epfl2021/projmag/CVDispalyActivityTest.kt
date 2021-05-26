package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdp13epfl2021.projmag.activities.CVDisplayActivity
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CVDispalyActivityTest {

    private val cv = CurriculumVitae(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean viverra sit amet purus vel sodales. Vestibulum ante mauris, sollicitudin sit amet tincidunt quis, ullamcorper sit amet turpis. Sed tincidunt mi dignissim, porta nibh at, cursus magna. Morbi feugiat vitae nunc id iaculis. Donec commodo sagittis sapien, ac feugiat neque sollicitudin sit amet. Donec ipsum metus, convallis vel ornare finibus, vulputate consequat magna. Pellentesque non nisi massa. In tristique turpis blandit velit laoreet, non vehicula tellus pulvinar. Nullam ornare elit ac pretium malesuada. Cras vel augue tristique, elementum mi et, rutrum urna. Proin vel auctor massa. Quisque faucibus vehicula elit sit amet maximus. Aenean ullamcorper rhoncus massa sit amet viverra. Duis non consequat nunc. Curabitur placerat feugiat augue ac dapibus. Donec iaculis nec magna sed feugiat.",
        listOf(
            CurriculumVitae.PeriodDescription(
                "name1",
                "location1",
                "description1",
                2010,
                2012
            )
        ),
        listOf(
            CurriculumVitae.PeriodDescription(
                "name1",
                "location1",
                "description1",
                2010,
                2012
            )
        ),
        listOf(
            CurriculumVitae.Language(
                "language1",
                CurriculumVitae.Language.Level.Basic
            )
        ),
        listOf(
            CurriculumVitae.SkillDescription(
                "skill1",
                CurriculumVitae.SkillDescription.SkillLevel.Expert
            )
        )
    )

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getIntent(): Intent {
        val intent = Intent(context, CVDisplayActivity::class.java)
        intent.putExtra(MainActivity.cv, cv as Parcelable)
        return intent
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<CVDisplayActivity> =
        ActivityScenarioRule(getIntent())

    @Test
    fun activityDisplayCorrectlyTheCv() {
        checkTextForView(R.id.cv_display_summary, cv.summary)


        

    }

    private fun checkTextForView(viewId: Int, expectedText: String) {
        onView(withId(viewId))
            .perform(scrollTo())
            .check(matches(withText(expectedText)))
    }

}