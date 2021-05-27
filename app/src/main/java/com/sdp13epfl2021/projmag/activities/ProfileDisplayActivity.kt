package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.ImmutableProfile

class ProfileDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_display)

        val profile: ImmutableProfile? = intent.getParcelableExtra(MainActivity.profile)
        profile?.let {
            setTextView(
                R.id.profile_display_firstname,
                R.string.profile_display_firstname,
                it.firstName
            )
            setTextView(
                R.id.profile_display_lastname,
                R.string.profile_display_lastname,
                it.lastName
            )
            setTextView(R.id.profile_display_age, R.string.profile_display_age, it.age.toString())
            setTextView(
                R.id.profile_display_gender,
                R.string.profile_display_gender,
                it.gender.toString()
            )
            setTextView(
                R.id.profile_display_sciper,
                R.string.profile_display_sciper,
                it.sciper.toString()
            )
            setTextView(R.id.profile_display_phone, R.string.profile_display_phone, it.phoneNumber)
            setTextView(
                R.id.profile_display_role,
                R.string.profile_display_role,
                it.role.toString()
            )
        } ?: run {
            Toast.makeText(
                this,
                getString(R.string.profile_display_profile_null),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun setTextView(viewId: Int, formatStringId: Int, valueText: String) {
        findViewById<TextView>(viewId).text = getString(formatStringId, valueText)
    }
}