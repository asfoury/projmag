package com.sdp13epfl2021.projmag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.adapter.OwnProjectAdapter
import com.sdp13epfl2021.projmag.model.ImmutableProject

class PersonalProjectList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_project_list)
        val viewListOwnProject = findViewById<View>(R.id.listOwnProject) as RecyclerView
        val adapter = OwnProjectAdapter(MainActivity.listOwnProject)
        viewListOwnProject.adapter = adapter
        viewListOwnProject.layoutManager = LinearLayoutManager(this)

    }
}