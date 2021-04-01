package com.sdp13epfl2021.projmag

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.sdp13epfl2021.projmag.database.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    init {//TODO remove after better initialization
        Utils.projectsDatabase
    }

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

    fun goToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToProjectsList() {
        val intent = Intent(this, ProjectsListActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun handleLink() {
        var fromLink = false
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
                pendingDynamicLinkData ->
            Log.i("MainActivity", "We have a dynamic link!")
            var deepLink: Uri? = null
            if (pendingDynamicLinkData != null) {
                deepLink = pendingDynamicLinkData.link!!
            }

            if (deepLink != null) {
                Log.i("MainActivity", "Here's the deep link URL:\n" + deepLink.toString())
                val currentPage: String? = deepLink.getQueryParameter("curPage")
                val curPage = Integer.parseInt(currentPage)
                //get page to go to
                val intent = Intent(this, ProfilePageActivity::class.java)
                startActivity(intent)
                finish()
                fromLink = true
            }
        }

        if (!fromLink) {
            goToProjectsList()
        }
    }

}

