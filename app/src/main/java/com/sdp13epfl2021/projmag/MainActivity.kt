package com.sdp13epfl2021.projmag

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.activities.SignInActivity
import com.sdp13epfl2021.projmag.activities.UserTypeChoice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object MainActivityCompanion {
        //These are used as name for extra in Intent
        const val fromLinkString: String = "fromLink"   //Boolean
        const val projectIdString: String = "projectId" //ProjectId
        const val projectString: String = "project"     //ImmutableProject
        const val cv: String = "CV"                     //CurriculumVitae
        const val profile: String = "profile"           //ImmutableProfile
        const val tagsList: String = "tagsList"         //Array string (originally of tags)
        const val sectionsList: String = "sectionsList" //Array string
    }

    @Inject
    @Named("currentUserId")
    lateinit var userId: String

    @Inject // TODO change with abstraction interface
    lateinit var firebaseDynLink: FirebaseDynamicLinks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserTypeChoice.isProfessor =
            getSharedPreferences(UserTypeChoice.savedTypeChoice, Context.MODE_PRIVATE).getBoolean(
                UserTypeChoice.isUserProf,
                true
            )

        /**If user is not authenticated, send him to SignInActivity to authenticate first.
         * Else send him to DashboardActivity*/
        Handler(Looper.getMainLooper()).postDelayed({
            if (userId.isNotEmpty()) {
                handleLink()
            } else {
                goToSignIn()
            }
        }, 2000)
    }

    private fun goToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToList(addExtras: (Intent) -> Unit) {
        val intent = Intent(this, ProjectsListActivity::class.java)
        addExtras(intent)
        startActivity(intent)
        finish()
    }

    private fun handleLink() {
        firebaseDynLink.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var dynamicLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    dynamicLink = pendingDynamicLinkData.link!!
                }
                var fromLink = false
                var projectId: String? = ""
                if (dynamicLink != null) {
                    projectId = dynamicLink.path?.substring(11)
                    fromLink = true
                }

                goToList { i ->
                    i.putExtra(fromLinkString, fromLink)
                    i.putExtra(projectIdString, projectId)
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, getString(R.string.failure), Toast.LENGTH_LONG)
                    .show()
                goToList {}
            }
    }
}