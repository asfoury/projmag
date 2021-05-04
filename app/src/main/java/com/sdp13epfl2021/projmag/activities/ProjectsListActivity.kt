package com.sdp13epfl2021.projmag.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.Form
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.fromLinkString
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.projectIdString
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ProjectFilter

class ProjectsListActivity : AppCompatActivity() {

    private lateinit var itemAdapter: ProjectAdapter
    private lateinit var recyclerView: RecyclerView
    private val appliedProjects: MutableList<ProjectId> = ArrayList()
    private lateinit var utils: Utils

    private fun updateAppliedProjects() {
        utils.userDataDatabase.getListOfAppliedToProjects({ list ->
            appliedProjects.removeAll { true }
            appliedProjects.addAll(list)
        },{})
    }

    fun getItemAdapter(): ProjectAdapter {
        return itemAdapter
    }

    fun getRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)

        utils = Utils.getInstance(this)
        updateAppliedProjects()

        val fromLink = intent.getBooleanExtra(fromLinkString, false)
        var projectId = ""
        if (fromLink) {
            projectId = intent.getStringExtra(projectIdString) ?: ""
        }

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_project)
        itemAdapter =
            ProjectAdapter(this, utils, recyclerView, fromLink, projectId)
        recyclerView.adapter = itemAdapter
        recyclerView.setHasFixedSize(false)

        // get the fab and make it go to the Form activity
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
        }


        if(!UserTypeChoice.isProfessor){
            fab.setVisibility(View.INVISIBLE)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_project_list, menu)
        val item = menu?.findItem(R.id.searchButton)
        val searchView: SearchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.profileButton -> {
                val intent = Intent(this, ProfilePageActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.filterButton -> {
                openFilterDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
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
                itemAdapter.projectFilter = ProjectFilter.default
                itemAdapter.filter.filter("")
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
        val pf = itemAdapter.projectFilter
        val view = layoutInflater.inflate(R.layout.filter_list_layout, null)
        view.findViewById<CheckBox>(R.id.filter_bachelor).isChecked = pf.bachelor
        view.findViewById<CheckBox>(R.id.filter_master).isChecked = pf.master
        view.findViewById<CheckBox>(R.id.filter_applied).isChecked = pf.applied
        return view
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
        itemAdapter.projectFilter = ProjectFilter(
            bachelor = bachelor,
            master = master,
            applied = applied,
            appliedProjects.toList()
        )
        itemAdapter.filter.filter("")
    }

    override fun onResume() {
        updateAppliedProjects()
        super.onResume()
    }


}
