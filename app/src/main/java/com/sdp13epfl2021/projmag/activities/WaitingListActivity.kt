package com.sdp13epfl2021.projmag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.CandidatureAdapter
import com.sdp13epfl2021.projmag.database.Utils

class WaitingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_list)

        val waitingListView: RecyclerView = findViewById(R.id.waiting_recycler_view)
        val projectId = intent.getStringExtra(MainActivity.projectIdString)

        projectId?.let {
            val waitingCVAdapter = CandidatureAdapter(this, Utils.getInstance(this), projectId)
            waitingListView.adapter = waitingCVAdapter
            waitingListView.layoutManager = LinearLayoutManager(this)
            waitingListView.setHasFixedSize(false)
        }
    }
}