package com.sdp13epfl2021.projmag.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.fromLinkString
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.projectIdString
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.Candidature
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ProjectFilter

/**
 * Displays a list of projects. User can filter based on various criteria and search by name.
 */
class ProjectsListActivity : AppCompatActivity() {

    private lateinit var projectAdapter: ProjectAdapter

    private lateinit var recyclerView: RecyclerView
    private val appliedProjects: MutableList<ProjectId> = ArrayList()
    private val favoriteList: MutableList<ProjectId> = ArrayList()
    private lateinit var utils: Utils
    private var userId: String? = null
    private var projectFilter: ProjectFilter = ProjectFilter()
    private var userPref: ProjectFilter = ProjectFilter()
    private var useFilterPref: Boolean = false
    private lateinit var candidatureDatabase: CandidatureDatabase

    /**
     * Creates and displays list of projects.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)

        utils = Utils.getInstance(this)
        userId = utils.auth.uid
        updateAppliedProjects()

        utils.userdataDatabase.getListOfFavoriteProjects({
            favoriteList.addAll(it)
        }, {})

        // if app was opened from deep link, extract relevant information to open the right project
        val fromLink = intent.getBooleanExtra(fromLinkString, false)
        var projectId = ""
        if (fromLink) {
            projectId = intent.getStringExtra(projectIdString) ?: ""
        }


        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_project)

        projectAdapter =
            ProjectAdapter(this, Utils.getInstance(this), recyclerView, fromLink, projectId)
        recyclerView.adapter = projectAdapter



        recyclerView.setHasFixedSize(false)

        setUpFab()
        addListenersToAppliedProjects()
    }

    private fun addListenersToAppliedProjects() {
        candidatureDatabase = utils.candidatureDatabase

        appliedProjects.forEach{
            utils.candidatureDatabase.addListener(it) { _: ProjectId, list : List<Candidature> ->
                val ownCandidatureThatChanged : Candidature? = list.find { candidature -> candidature.userId == utils.auth.currentUser?.uid }
                if(ownCandidatureThatChanged?.state == Candidature.State.Accepted) {
                    val otherCandidatures = appliedProjects.filter { projectId -> (ownCandidatureThatChanged.projectId != projectId) }
                    otherCandidatures.forEach { otherCandidatureId ->
                        utils.auth.currentUser?.uid?.let {
                                uid ->
                            candidatureDatabase.removeCandidature(
                                otherCandidatureId,
                                uid,
                                {},
                                {}
                            )
                        }
                        utils.userdataDatabase.applyUnapply(false, otherCandidatureId, {}, {})
                    }
                }
            }
        }
    }

    private fun setUpFab() {
        // get the fab and make it go to the Form activity
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ProjectCreationActivity::class.java)
            startActivity(intent)
        }

        if (!UserTypeChoice.isProfessor) {
            fab.visibility = View.INVISIBLE
        }
    }

    /**
     * Adds search button and functionality, filter button, user button to menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_project_list, menu)
        val item = menu?.findItem(R.id.searchButton)
        val searchView: SearchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                projectAdapter.getFilter(projectFilter).filter(newText)

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.profileButton -> {
                val intent = Intent(this, ProfileEditPageActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.filterButton -> {
                openFilterDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onResume() {
        updateAppliedProjects()
        updateFavoriteProjects()
        updatePreferences()
        super.onResume()
    }


    /**
     * Update the list of projects, which the user applied to, from the Database
     */
    private fun updateAppliedProjects() {
        utils.userdataDatabase.getListOfAppliedToProjects({ list ->
            appliedProjects.clear()
            appliedProjects.addAll(list)
        }, {})
    }

    private fun updateFavoriteProjects() {
        utils.userdataDatabase.getListOfFavoriteProjects({ list ->
            favoriteList.clear()
            favoriteList.addAll(list)
        }, {})
    }


    /**
     * TODO Remove once the tests have been fixed
     */
    fun getItemAdapter(): ProjectAdapter {
        return projectAdapter
    }

    /**
     * TODO Remove once the tests have been fixed
     */
    fun getRecyclerView(): RecyclerView {
        return recyclerView
    }

    /**
     * Update the current ProjectFilter with the given value,
     * and updates its applicationCheck function
     *
     * @param pf the new ProjectFilter
     */
    private fun setProjectFilter(pf: ProjectFilter?) {
        pf?.apply {
            setApplicationCheck { checkIfApplied(it) }
            setFavoriteCheck { checkIfFavorite(it) }
            setOwnCheck { checkIfOwn(it) }
            projectFilter = this
        }
    }


    /**
     * Opens a dialog with filtering options for the project list
     */
    private fun openFilterDialog() {
        val builder = AlertDialog.Builder(this)
        val view = constructDialogView()
        builder
            .setView(view)
            .setNeutralButton(getString(R.string.clear)) { _, _ ->

                projectFilter = ProjectFilter()
                projectAdapter.filter.filter("")

            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                filter(view)
            }.show()
    }

    /**
     * The filter view with initialised parameters
     *
     * @return Dialog view
     */
    @SuppressLint("InflateParams")
    private fun constructDialogView(): View {

        val pf = projectFilter
        val view = layoutInflater.inflate(R.layout.project_filter_settings, null)

        val applied = view.findViewById<CheckBox>(R.id.filter_applied)
        val own = view.findViewById<CheckBox>(R.id.filter_own)

        if (UserTypeChoice.isProfessor) {
            applied.visibility = View.INVISIBLE
        } else {
            own.visibility = View.INVISIBLE
        }

        view.findViewById<CheckBox>(R.id.filter_bachelor).isChecked = pf.bachelor
        view.findViewById<CheckBox>(R.id.filter_master).isChecked = pf.master
        applied.isChecked = pf.applied
        own.isChecked = pf.own
        view.findViewById<CheckBox>(R.id.filter_favorites).isChecked = pf.favorite

        view.findViewById<ImageButton>(R.id.filter_settings_button).setOnClickListener {
            startActivity(Intent(this, PreferencesActivity::class.java))
        }

        setUpPreferencesSwitch(view)

        return view
    }

    private fun setUpPreferencesSwitch(view: View) {
        view.findViewById<SwitchCompat>(R.id.filter_preferences_switch).apply {
            setOnCheckedChangeListener { _, isChecked ->
                useFilterPref = isChecked

                view.findViewById<View>(R.id.filter_preferences_layout).visibility =
                    if (isChecked) View.GONE else View.VISIBLE
            }
            isChecked = useFilterPref
        }
    }

    /**
     * Update the project filter, adapter and the list, with the
     * data given in the view.
     *
     * @param view The dialog view with data, given by the user
     */
    private fun filter(view: View) {
        val bachelor = view.findViewById<CheckBox>(R.id.filter_bachelor).isChecked
        val master = view.findViewById<CheckBox>(R.id.filter_master).isChecked
        val applied = view.findViewById<CheckBox>(R.id.filter_applied).isChecked
        val favorite = view.findViewById<CheckBox>(R.id.filter_favorites).isChecked
        val own = view.findViewById<CheckBox>(R.id.filter_own).isChecked

        setProjectFilter(
            if (useFilterPref) {
                userPref
            } else {
                ProjectFilter(
                    bachelor = bachelor,
                    master = master,
                    applied = applied,
                    favorite = favorite,
                    own = own
                )
            }
        )
        projectAdapter.getFilter(projectFilter).filter("")

    }

    /**
     * Check if the user applied to the given project
     *
     * @param project the project to check
     * @return `true` if the user applied, `false` otherwise
     */
    private fun checkIfApplied(project: ImmutableProject): Boolean =
        appliedProjects.contains(project.id)

    /**
     * Checks if the project is contained in the current user favorite list
     *
     * @param project the project to check
     * @return true if the project is in the favorites, false else
     */
    private fun checkIfFavorite(project: ImmutableProject): Boolean =
        favoriteList.contains(project.id)

    /**
     * Checks if the project was made by the current user
     *
     * @param project the project to check
     * @return true if the project was made by the user, false else
     */
    private fun checkIfOwn(project: ImmutableProject): Boolean {
        return userId != null && project.authorId == userId
    }


    /**
     * Fetch the user preference from Database and update.
     */
    private fun updatePreferences() {
        utils.userdataDatabase.getPreferences(
            { pf -> pf?.let { userPref = it } },
            {}
        )
    }

}
