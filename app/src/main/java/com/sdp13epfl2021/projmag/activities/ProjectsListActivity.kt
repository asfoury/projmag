package com.sdp13epfl2021.projmag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.ItemAdapter
import com.sdp13epfl2021.projmag.data.Datasource

class ProjectsListActivity : AppCompatActivity() {
    private lateinit var itemAdapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)
        setContentView(R.layout.activity_projects_list)
        val myDataset = Datasource().loadProjects().toMutableList()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        itemAdapter = ItemAdapter(this, myDataset)
        recyclerView.adapter = itemAdapter
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_project_list, menu)
        val item = menu?.findItem(R.id.action_search)
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
}