package com.sdp13epfl2021.projmag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity
import android.os.Looper
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
            if(user != null){
                goToProjectsList()
            }else{
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


}

