package com.sdp13epfl2021.projmag

import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.sdp13epfl2021.projmag.JavaToKotlinHelperAndroidTest.anyObject
import com.sdp13epfl2021.projmag.database.di.*
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito
import javax.inject.Named


@UninstallModules(
    ProjectDatabaseModule::class,
    UserdataDatabaseModule::class,
    CandidatureDatabaseModule::class,
    UserIdModule::class,
    FirebaseDynamicLinksModule::class
)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var testRule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
        .around(ActivityScenarioRule(MainActivity::class.java))

    @BindValue
    @Named("currentUserId")
    val userId: String = "uid"

    @BindValue
    val firebaseDynLinks: FirebaseDynamicLinks = Mockito.mock(FirebaseDynamicLinks::class.java)


    // this is because we go to ProjectListActivity
    @BindValue
    val projectDB: ProjectDatabase = Mockito.mock(ProjectDatabase::class.java).also { db ->
        Mockito.`when`(db.getAllProjects(anyObject(), anyObject())).then {
            @Suppress("UNCHECKED_CAST")
            val onSuccess = it.arguments[0] as Function1<List<ImmutableProject>, Unit>
            onSuccess(listOf())
        }

        Mockito.`when`(db.addProjectsChangeListener(anyObject())).then {
            /* Does nothing for now */
        }
    }

    // this is because we go to ProjectListActivity
    @BindValue
    val userDB: UserdataDatabase = Mockito.mock(UserdataDatabase::class.java)

    @BindValue
    val candidatureDatabase: CandidatureDatabase = Mockito.mock(CandidatureDatabase::class.java)


    @Suppress("UNCHECKED_CAST")
    val mockTask: Task<PendingDynamicLinkData> =
        (Mockito.mock(Task::class.java) as Task<PendingDynamicLinkData>)

    private val mockPenDynLink: PendingDynamicLinkData =
        Mockito.mock(PendingDynamicLinkData::class.java)

    private val mockUri: Uri = Mockito.mock(Uri::class.java)


    @Test
    fun handleLinkWorks() {
        // TODO : `11` is and implementation detail, should be fixed
        Mockito.`when`(mockUri.path).thenReturn("0".repeat(11) + "userID")
        Mockito.`when`(mockPenDynLink.link).thenReturn(mockUri)
        Mockito.`when`(mockTask.addOnSuccessListener(anyObject())).then {
            @Suppress("UNCHECKED_CAST")
            val f = it.arguments[0] as OnSuccessListener<PendingDynamicLinkData>
            f.onSuccess(mockPenDynLink)
            mockTask
        }
        Mockito.`when`(mockTask.addOnFailureListener(anyObject())).thenReturn(mockTask)
        Mockito.`when`(firebaseDynLinks.getDynamicLink(anyObject<Intent>())).thenReturn(mockTask)

        Thread.sleep(5000) // wait for activity to start
    }
}