package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.ImmutableProject

class ProjectInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        // get all the text views that will be set
        val title = findViewById<TextView>(R.id.info_project_title)
        val lab = findViewById<TextView>(R.id.info_lab_name)
        val description = findViewById<TextView>(R.id.info_description)
        val nbOfStudents = findViewById<TextView>(R.id.info_nb_students)
        val type = findViewById<TextView>(R.id.info_available_for)
        val responsible = findViewById<TextView>(R.id.info_responsible_name)


        // get the project
        val project: ImmutableProject? = intent.getParcelableExtra("project")

        // set the text views
        title.text = project?.name
        lab.text = project?.lab
        description.text = project?.description
        nbOfStudents.text = "1"
        type.text = "Bachelor"
        responsible.text = project?.teacher

        // make the back button in the title bar work
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)



    }


}