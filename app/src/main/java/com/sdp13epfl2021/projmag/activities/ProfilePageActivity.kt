package com.sdp13epfl2021.projmag.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.impl.firebase.UserProfileDatabase
import com.sdp13epfl2021.projmag.model.*

/**
 * Activity in which one can create their profile by filling in fields
 * such as names, age, sciper, etc...
 */
class ProfilePageActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var buttonAddCv: Button
    lateinit var buttonSubChange: Button
    private val pickImage = 0
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        buttonAddCv = findViewById(R.id.button_add_cv)
        buttonSubChange = findViewById(R.id.buttonSubChangeProfil)

        if(UserTypeChoice.isProfessor){
            findViewById<TextView>(R.id.profile_sciper).setVisibility(View.INVISIBLE)
            //buttonAddCv.setVisibility(View.INVISIBLE)
        }

        UserProfileDatabase(Firebase.firestore, Firebase.auth).getProfile(::loadUserProfile) {
            Toast.makeText(this,getString(R.string.profile_loading_failed)   , Toast.LENGTH_LONG).show()
        }


        buttonAddCv.setOnClickListener{
            val intent = Intent(this,CVCreationActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonSubChange.setOnClickListener{
            val profile = createProfileFromFields()
            if(profile != null) {
                UserProfileDatabase(Firebase.firestore, Firebase.auth).uploadProfile(profile, {}, {})
            }
            val intent = Intent(this,ProjectsListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadUserProfile(profile : ImmutableProfile?) {
        if (profile != null) {
            findViewById<EditText>(R.id.profile_firstname).setText(profile.firstName)
            findViewById<EditText>(R.id.profile_lastname).setText(profile.lastName)
            findViewById<EditText>(R.id.profile_age).setText(profile.age.toString())
            findViewById<EditText>(R.id.profile_genre).setText(profile.gender.toString())
            findViewById<EditText>(R.id.profile_phone_number).setText(profile.phoneNumber)
            findViewById<EditText>(R.id.profile_sciper).setText(profile.sciper.toString())
        } else {
            Toast.makeText(this, getString(R.string.profile_loading_failed), Toast.LENGTH_LONG).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun createProfileFromFields() : ImmutableProfile? {
        val firstName = findViewById<EditText>(R.id.profile_firstname).text.toString()
        val lastName = findViewById<EditText>(R.id.profile_lastname).text.toString()
        val age = findViewById<EditText>(R.id.profile_age).text.toString()

        val gender = when(findViewById<EditText>(R.id.profile_genre).text.toString()){
            Gender.MALE.name -> Gender.MALE
            Gender.FEMALE.name -> Gender.FEMALE
            else -> Gender.OTHER
        }
        val phoneNumber = findViewById<EditText>(R.id.profile_phone_number).text.toString()
        val role = Role.STUDENT
        val sciper = findViewById<EditText>(R.id.profile_sciper).text.toString()


        val profile =  ImmutableProfile.build(lastName,firstName,Integer.valueOf(age),gender , Integer.valueOf(sciper) ,phoneNumber,
            role)
        return when(profile) {
            is Success -> {
                profile.value
            }
            is Failure -> {
                null
            }
        }
    }

}