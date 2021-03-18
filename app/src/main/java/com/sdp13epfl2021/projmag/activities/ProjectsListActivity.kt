package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.Form
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.ItemAdapter
import com.sdp13epfl2021.projmag.data.Datasource
import com.sdp13epfl2021.projmag.model.Project

class ProjectsListActivity : AppCompatActivity() {
    val favoriteList = mutableListOf<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects_list)
        setContentView(R.layout.activity_projects_list)
        val myDataset = Datasource().loadProjects()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)
    }
    fun addOnFavoriteList(view: View){

        val intent = Intent(this, Form::class.java)
        startActivity(intent)
    }


}