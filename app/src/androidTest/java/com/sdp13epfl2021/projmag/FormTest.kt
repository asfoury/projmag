package com.sdp13epfl2021.projmag

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FormTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<Form> =
        ActivityScenarioRule(Form::class.java)

    @Test
    fun writeAProjectToSubmit() {
        // need to swipe down to make sure textFields are visible when running tests
        onView(withId(R.id.project_submission_scrollview))
            .perform(ViewActions.swipeDown())

        Thread.sleep(2000)

        onView(withId(R.id.form_edit_text_laboratory))
            .perform(replaceText("Lab Name"))

        onView(withId(R.id.form_edit_text_project_name))
            .perform(replaceText("Project Name"))

        onView(withId(R.id.form_edit_text_teacher))
            .perform(replaceText("Professor Name"))

        onView(withId(R.id.form_edit_text_project_TA))
            .perform(replaceText("TA Name"))

        onView(withId(R.id.form_nb_of_participant))
            .perform(replaceText("2"))

        onView(withId(R.id.form_check_box_MP))
            .perform(click())

        onView(withId(R.id.form_check_box_SP))
            .perform(click())

        onView(withId(R.id.form_project_description))
            .perform(
                replaceText(
                    """
                Hannah Glasse (née Allgood; March 1708 – 1 September 1770) was an English cookery writer of the 18th century. Her first cookery book, The Art of Cookery Made Plain and Easy, published in 1747, became the best-selling recipe book that century. 
                It was reprinted within its first year of publication, appeared in 20 editions in the 18th century, and continued to be published until well into the 19th century. 
                She later wrote The Servants' Directory (1760) and The Compleat Confectioner, which was probably published in 1760; neither book was as commercially successful as her first.
                Glasse was born in London to a Northumberland landowner and his mistress. After the relationship ended, Glasse was brought up in her father's family. 
                When she was 16 she eloped with a 30-year-old Irish subaltern then on half-pay and lived in Essex, working on the estate of the Earls of Donegall. The couple struggled financially and, with the aim of raising money, 
                Glasse wrote The Art of Cookery. She copied extensively from other cookery books, around a third of the recipes having been published elsewhere. 
                Among her original recipes are the first known curry recipe written in English, as well as three recipes for pilau, an early reference to vanilla in English cuisine, 
                the first recorded use of jelly in trifle, and an early recipe for ice cream. She was also the first to use the term "Yorkshire pudding" in print.
                Glasse became a dressmaker in Covent Garden—where her clients included Princess Augusta, the Princess of Wales—but she ran up excessive debts. She was imprisoned for bankruptcy and was forced to sell the copyright of The Art of Cookery. 
                Much of Glasse's later life is unrecorded; information about her identity was lost until uncovered in 1938 by the historian Madeleine Hope Dodds.
            """.trimIndent()
                )
            )
    }
}