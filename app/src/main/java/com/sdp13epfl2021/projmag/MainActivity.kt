package com.sdp13epfl2021.projmag

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import com.sdp13epfl2021.projmag.activities.SignInActivity

class MainActivity : AppCompatActivity() {
    companion object MainActivityCompanion {
        const val fromLinkString: String = "fromLink"
        const val projectIdString: String = "projectId"
        const val cv: String = "CV"
        const val profile: String = "profile"
    }

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        /**If user is not authenticated, send him to SignInActivity to authenticate first.
         * Else send him to DashboardActivity*/
        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
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
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
                pendingDynamicLinkData ->
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
            Toast.makeText(applicationContext, getString(R.string.failure), Toast.LENGTH_LONG).show()
            goToList({})
        }
    }
}