package com.sdp13epfl2021.projmag

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity

class MainActivity : AppCompatActivity() {

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

    fun goToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }



    fun handleLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
                pendingDynamicLinkData ->
            Log.i("MainActivity", "We have a dynamic link!")
            var dynamicLink: Uri? = null
            if (pendingDynamicLinkData != null) {
                dynamicLink = pendingDynamicLinkData.link!!
            }
            var fromLink = false
            var projectId: String? = ""
            if (dynamicLink != null) {
                Log.i("MainActivity", "Here's the deep link URL:\n" + dynamicLink.toString())
                projectId = dynamicLink.path?.substring(11)
                fromLink = true
            }

            val intent = Intent(this, ProjectsListActivity::class.java)
            intent.putExtra("fromLink", fromLink)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "failure", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProjectsListActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}

