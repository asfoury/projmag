package com.sdp13epfl2021.projmag.activities


import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.local.LruGarbageCollector
import com.google.firebase.ktx.Firebase
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.UserProfileDatabase
import com.sdp13epfl2021.projmag.model.*


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

        imageView = findViewById(R.id.image_profile)
        button = findViewById(R.id.button_edit_profile)
        buttonAddCv = findViewById(R.id.button_add_cv)
        buttonSubChange = findViewById(R.id.buttonSubChangeProfil)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }


        UserProfileDatabase(Firebase.firestore, Firebase.auth).getProfile(::loadUserProfile)

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
            Log.d(ContentValues.TAG, "NOT NULL")
            findViewById<EditText>(R.id.profile_firstname).setText(profile.firstName)
            findViewById<EditText>(R.id.profile_lastname).setText(profile.lastName)
            findViewById<EditText>(R.id.profile_age).setText(profile.age.toString())
            findViewById<EditText>(R.id.profile_genre).setText(profile.gender.toString())
            findViewById<EditText>(R.id.profile_phone_number).setText(profile.phoneNumber)
            findViewById<EditText>(R.id.profile_sciper).setText(profile.sciper.toString())
        } else {
            Log.d(ContentValues.TAG, "NULL")
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
        val gender = if (findViewById<EditText>(R.id.profile_genre).text.toString() == Gender.MALE.toString())  Gender.MALE
        else {
            if (findViewById<EditText>(R.id.profile_genre).text.toString() == Gender.FEMALE.toString()) Gender.FEMALE else Gender.OTHER
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