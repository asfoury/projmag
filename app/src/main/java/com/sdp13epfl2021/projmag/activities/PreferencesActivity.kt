package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.model.ProjectFilter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * An activity where the user can set it's preferences for projects.
 * These preferences are pushed to the Database
 */
@AndroidEntryPoint
class PreferencesActivity : AppCompatActivity() {
    /**
     * The database of user's information
     */
    @Inject
    lateinit var userDB: UserdataDatabase

    /**
     * Check box that require the project to ask for a bachelor degree
     */
    private lateinit var bachelor: CheckBox

    /**
     * Check box that require the project to ask for a master degree
     */
    private lateinit var master: CheckBox

    /**
     * Check box that require the project to be applied by the user
     */
    private lateinit var applied: CheckBox

    /**
     * Check box that require the project to be in the user's favorites
     */
    private lateinit var favorite: CheckBox

    /**
     * Check box that require the project to be in the user's created projects
     */
    private lateinit var own: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_filter_settings)

        // Update the view for this activity
        findViewById<TextView>(R.id.filter_title).text = getString(R.string.preferences)
        findViewById<View>(R.id.filter_preferences_switch).visibility = View.GONE
        findViewById<View>(R.id.filter_settings_button).visibility = View.GONE
        // By default GONE, but here we need it
        findViewById<Button>(R.id.filter_pref_submit_button).apply {
            visibility = View.VISIBLE
            setOnClickListener { submit() }
        }

        bachelor = findViewById(R.id.filter_bachelor)
        master = findViewById(R.id.filter_master)
        applied = findViewById(R.id.filter_applied)
        favorite = findViewById(R.id.filter_favorites)
        own = findViewById(R.id.filter_own)

        if (UserTypeChoice.isProfessor) {
            applied.visibility = View.INVISIBLE
        } else {
            own.visibility = View.INVISIBLE
        }

        userDB.getPreferences(
            { pf ->
                pf?.let {
                    runOnUiThread { updateUiFromFilter(it) }
                }
            },
            {}
        )
    }

    /**
     * Display the given message as a long length `Toast` message
     *
     * @param msg the message to be displayed
     */
    private fun toast(msg: String) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Build a `ProjectFilter` from UI information.
     *
     * @return a ProjectFilter from UI information
     */
    private fun buildProjectFilter(): ProjectFilter =
        ProjectFilter(
            bachelor = bachelor.isChecked,
            master = master.isChecked,
            applied = applied.isChecked,
            favorite = favorite.isChecked,
            own = own.isChecked
        )

    /**
     * Build UI from the given ProjectFilter
     *
     * @param pf ProjectFilter to build UI from
     */
    private fun updateUiFromFilter(pf: ProjectFilter) {
        bachelor.isChecked = pf.bachelor
        master.isChecked = pf.master
        applied.isChecked = pf.applied
        favorite.isChecked = pf.favorite
        own.isChecked = pf.own
    }

    /**
     * Push the ProjectFilter, built from UI information, to the Database
     * If it succeeded, finishes the activity, otherwise display a message to the user
     */
    private fun submit() {
        val pf = buildProjectFilter()
        toast(getString(R.string.sending_preferences))
        userDB.pushPreferences(
            pf,
            {},
            { toast(getString(R.string.failed_send_pref)) }
        )
        finish()
    }
}