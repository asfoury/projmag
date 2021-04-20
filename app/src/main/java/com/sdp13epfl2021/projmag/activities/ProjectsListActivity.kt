package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.Form
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.fromLinkString
import com.sdp13epfl2021.projmag.MainActivity.MainActivityCompanion.projectIdString
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.ProjectAdapter
import com.sdp13epfl2021.projmag.database.Utils

class ProjectsListActivity : AppCompatActivity() {
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var recyclerView: RecyclerView

    public fun getItemAdapter(): ProjectAdapter {
        return projectAdapter
    }

    public fun getRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)

        val fromLink = intent.getBooleanExtra(fromLinkString, false)
        var projectId = ""
        if (fromLink) {
            projectId = intent.getStringExtra(projectIdString) ?: ""
        }

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_project)
        projectAdapter = ProjectAdapter(this, Utils(this), recyclerView, fromLink, projectId)
        recyclerView.adapter = projectAdapter
        recyclerView.setHasFixedSize(false)

        // get the fab and make it go to the Form activity
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
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
                projectAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profileButton) {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
