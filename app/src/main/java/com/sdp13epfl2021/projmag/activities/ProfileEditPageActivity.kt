package com.sdp13epfl2021.projmag.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

/**
 * Activity in which one can create their profile by filling in fields
 * such as names, age, sciper, etc...
 */
@AndroidEntryPoint
class ProfileEditPageActivity : AppCompatActivity() {


    @Inject
    lateinit var userdataDatabase: UserdataDatabase

    @Inject
    @Named("currentUserId")
    lateinit var userID: String

    lateinit var imageView: ImageView
    lateinit var genderSpinner: Spinner
    private val pickImage = 0
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)


        if (UserTypeChoice.isProfessor) {
            findViewById<TextView>(R.id.profile_sciper).visibility = View.INVISIBLE
            //buttonAddCv.setVisibility(View.INVISIBLE)
        }

        genderSpinner = findViewById(R.id.profile_gender_spinner)
        genderSpinner.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            Gender.values()
        )

        userdataDatabase.getProfile(
            userID,
            ::loadUserProfile
        ) { showToast(getString(R.string.profile_loading_failed)) }

        findViewById<Button>(R.id.button_show_cv).setOnClickListener {
            userdataDatabase.getCv(userID, { cv ->
                startActivityAndFinish(
                    CVDisplayActivity::class.java,
                    Pair(MainActivity.cv, cv as Parcelable?)
                )
            }, { showToast(getString(R.string.cv_loading_failed)) })
        }

        findViewById<Button>(R.id.button_add_cv).setOnClickListener {
            startActivityAndFinish(CVCreationActivity::class.java)
        }

        findViewById<Button>(R.id.buttonSubChangeProfil).setOnClickListener {
            val profile = createProfileFromFields()

            if (profile != null) {
                userdataDatabase.uploadProfile(profile, {}, {})
            }
            startActivityAndFinish(ProjectsListActivity::class.java)
        }

    }

    private fun showToast(msg: String) {
        runOnUiThread {
            Toast.makeText(
                this,
                msg,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Start an activity with optional data in the intent, then finish.
     *
     * @param cls Class of the activity to start.
     * @param extra an optional pair of name and parcelable data.
     */
    private fun <T> startActivityAndFinish(
        cls: Class<T>,
        extra: Pair<String, Parcelable?>? = null
    ) {
        val intent = Intent(this, cls)
        extra?.let {
            intent.putExtra(extra.first, extra.second)
        }
        startActivity(intent)
        finish()
    }

    private fun loadUserProfile(profile: ImmutableProfile?) {
        if (profile != null) {
            findViewById<EditText>(R.id.profile_firstname).setText(profile.firstName)
            findViewById<EditText>(R.id.profile_lastname).setText(profile.lastName)
            findViewById<EditText>(R.id.profile_age).setText(profile.age.toString())
            genderSpinner.setSelection(profile.gender.ordinal)
            findViewById<EditText>(R.id.profile_phone_number).setText(profile.phoneNumber)
            findViewById<EditText>(R.id.profile_sciper).setText(profile.sciper.toString())
        } else {
            showToast(getString(R.string.profile_loading_failed))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun createProfileFromFields(): ImmutableProfile? {
        val firstName = findViewById<EditText>(R.id.profile_firstname).text.toString()
        val lastName = findViewById<EditText>(R.id.profile_lastname).text.toString()
        val age = findViewById<EditText>(R.id.profile_age).text.toString()
        val gender = genderSpinner.selectedItem as Gender
        val phoneNumber = findViewById<EditText>(R.id.profile_phone_number).text.toString()
        val role = Role.STUDENT
        val sciper = findViewById<EditText>(R.id.profile_sciper).text.toString()


        val profile = ImmutableProfile.build(
            lastName, firstName, Integer.valueOf(age), gender, sciper.toIntOrNull(), phoneNumber,
            role
        )
        return when (profile) {
            is Success -> {
                profile.value
            }
            is Failure -> {
                null
            }
        }
    }

}