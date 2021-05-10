package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.firebase.auth.FirebaseAuth
import com.sdp13epfl2021.projmag.activities.WaitingListActivity
import com.sdp13epfl2021.projmag.adapter.CandidatureAdapter
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.*
import com.sdp13epfl2021.projmag.database.fake.*
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@LargeTest
@RunWith(AndroidJUnit4::class)
class WaitingListTest {

    val auth: FirebaseAuth = Mockito.mock(FirebaseAuth::class.java)

    val pid = "J890ghj34sdklv3245"
    val project = ImmutableProject(
        pid,
        "What fraction of Google searches are answered by Wikipedia?",
        "DLAB",
        "authorID11",
        "Robert West",
        "TA1",
        1,
        listOf<String>(),
        false,
        true,
        listOf(
            "data analysis",
            "large datasets",
            "database",
            "systems",
            "database",
            "systems"
        ),
        false,
        "Description of project1",
        listOf(),
        listOf("Architecture")

    )

    val uid1 = "some-id-001"
    val uid2 = "some-id-002"
    val uid3 = "some-id-003"
    val uid4 = "some-id-004"
    val cand1 = dummyCandidature(pid, uid1, Candidature.State.Waiting)
    val cand2 = dummyCandidature(pid, uid2, Candidature.State.Waiting)
    val cand3 = dummyCandidature(pid, uid3, Candidature.State.Rejected)
    val cand4 = dummyCandidature(pid, uid4, Candidature.State.Accepted)

    val projectsDB = FakeProjectDatabase(listOf(project))
    val userdataDB = FakeUserdataDatabase()
    val fileDB = FakeFileDatabase()
    val candidatureDB = FakeCandidatureDatabase(
        mapOf(pid to mapOf(
            uid1 to Candidature.State.Waiting,
            uid2 to Candidature.State.Waiting,
            uid3 to Candidature.State.Rejected,
            uid4 to Candidature.State.Accepted,
        )).toMutableMap(),
        mapOf(pid to mapOf(
            uid1 to cand1,
            uid2 to cand2,
            uid3 to cand3,
            uid4 to cand4
        )).toMutableMap()
    )
    val metadataDB = FakeMetadataDatabase()
    val context: Context = ApplicationProvider.getApplicationContext()
    val utils = Utils.getInstance(context, true, auth, userdataDB, candidatureDB, fileDB, metadataDB, projectsDB)

    private fun getIntent(): Intent {
        val intent = Intent(context, WaitingListActivity::class.java)
        intent.putExtra(MainActivity.projectIdString, pid)
        //scenario = ActivityScenario.launch(intent)
        return intent
    }

    @get:Rule var activityScenarioRule = ActivityScenarioRule<WaitingListActivity>(getIntent())

    @After
    fun clean() {
        //reset the Utils instance to default one
        Utils.getInstance(context, true)
    }

    private fun dummyCandidature(projectId: ProjectId, userID: String, state: Candidature.State): Candidature {
        return Candidature(projectId, userID, dummyProfile(userID), dummyCV(userID), state)
    }

    private fun dummyProfile(userID: String): ImmutableProfile {
        return (ImmutableProfile.build(
            userID,
            userID,
            21,
            Gender.MALE,
            123456,
            "021 123 45 67", Role.STUDENT
        ) as Success).value
    }

    private fun dummyCV(userID: String): CurriculumVitae {
        return CurriculumVitae(
            "summary of $userID",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    @Test
    fun waitingListEspressoTest() {
        val recyclerMatchers = ViewMatchers.withId(R.id.waiting_recycler_view)
        val recyclerView = onView(recyclerMatchers)

        scrollTo(recyclerView, 0)
        assertCardColor(Color.WHITE, 0, recyclerMatchers)
        assertFields(uid1, uid1, "IC", 0, recyclerMatchers)

        scrollTo(recyclerView, 1)
        assertCardColor(Color.WHITE, 1, recyclerMatchers)
        assertFields(uid2, uid2, "IC", 1, recyclerMatchers)

        scrollTo(recyclerView, 2)
        assertCardColor(Color.RED, 2, recyclerMatchers)
        assertFields(uid3, uid3, "IC", 2, recyclerMatchers)

        scrollTo(recyclerView, 3)
        assertCardColor(Color.GREEN, 3, recyclerMatchers)
        assertFields(uid4, uid4, "IC", 3, recyclerMatchers)


        scrollTo(recyclerView, 2)
        clickAccept(recyclerMatchers, 2)
        assertCardColor(Color.GREEN, 2, recyclerMatchers)
        clickAccept(recyclerMatchers, 2)
        assertCardColor(Color.GREEN, 2, recyclerMatchers)
        clickReject(recyclerMatchers, 2)
        assertCardColor(Color.RED, 2, recyclerMatchers)
        clickReject(recyclerMatchers, 2)
        assertCardColor(Color.RED, 2, recyclerMatchers)

    }

//    @Test
//    fun canClickProfile() {
//        val recyclerMatchers = ViewMatchers.withId(R.id.waiting_recycler_view)
//        val recyclerView = onView(recyclerMatchers)
//        scrollTo(recyclerView, 2)
//        clickProfile(recyclerMatchers, 2)
//    }
//
//    @Test
//    fun canClickCV() {
//        val recyclerMatchers = ViewMatchers.withId(R.id.waiting_recycler_view)
//        val recyclerView = onView(recyclerMatchers)
//        scrollTo(recyclerView, 2)
//        clickCV(recyclerMatchers, 2)
//    }

    private fun scrollTo(recyclerView: ViewInteraction, pos: Int) {
        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<CandidatureAdapter.CandidatureHolder>(
                pos
            )
        )
    }

    private fun clickAccept(recycler: Matcher<View>, pos: Int) {
        clickButton(recycler, pos, R.id.waiting_accept)
    }
    private fun clickReject(recycler: Matcher<View>, pos: Int) {
        clickButton(recycler, pos, R.id.waiting_reject)
    }
    private fun clickProfile(recycler: Matcher<View>, pos: Int) {
        clickButton(recycler, pos, R.id.waiting_profile)
    }
    private fun clickCV(recycler: Matcher<View>, pos: Int) {
        clickButton(recycler, pos, R.id.waiting_cv)
    }
    private fun clickButton(recycler: Matcher<View>, pos: Int, id: Int) {
        val card = Matchers.allOf(childAtPosition(recycler, pos))
        onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withParent(card))),
                ViewMatchers.withId(id),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
    }

    private fun assertCardColor(color: Int, pos: Int, recycler: Matcher<View>) {
        onView(
            childAtPosition(recycler, pos)
        ).check { view, _ ->
            assertTrue(view is CardView)
            val cardView: CardView = view as CardView
            assertEquals(color, cardView.cardBackgroundColor.defaultColor)
        }
    }

    private fun assertFields(
        firstname: String,
        lastName: String,
        section: String,
        pos: Int,
        recycler: Matcher<View>
    ) {
        val card = Matchers.allOf(childAtPosition(recycler, pos))

        onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(card)),
                ViewMatchers.withId(R.id.waiting_firstname),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(CardView::class.java))),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("First name : $firstname")))

        onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(card)),
                ViewMatchers.withId(R.id.waiting_lastname),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(CardView::class.java))),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("Last name : $lastName")))

        onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(card)),
                ViewMatchers.withId(R.id.waiting_section),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(CardView::class.java))),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText("Section : $section")))
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