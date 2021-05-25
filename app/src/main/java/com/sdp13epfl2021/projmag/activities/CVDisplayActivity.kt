package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.CVAdapter
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae

class CVDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cvdisplay)

        val cv: CurriculumVitae? = intent.getParcelableExtra(MainActivity.cv)

        cv?.let {
            findViewById<TextView>(R.id.cv_display_summary).text = cv.summary

            val cvListView: RecyclerView = findViewById(R.id.cv_display_recycler)
            val cvAdapter = CVAdapter(this, cv)

            cvListView.adapter = cvAdapter
            cvListView.layoutManager = LinearLayoutManager(this)
            cvListView.setHasFixedSize(true)
        } ?: run {
            Toast.makeText(this, getString(R.string.cv_display_cv_null), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}