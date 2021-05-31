package com.sdp13epfl2021.projmag.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.CandidatureAdapter
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity which displays a list of those who have applied to a project submitted
 * by the user.
 */
@AndroidEntryPoint
class WaitingListActivity : AppCompatActivity() {

    @Inject
    lateinit var candidatureDB: CandidatureDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_list)

        val waitingListView: RecyclerView = findViewById(R.id.waiting_recycler_view)
        val projectId = intent.getStringExtra(MainActivity.projectIdString)

        projectId?.let {
            val waitingCVAdapter = CandidatureAdapter(this, candidatureDB, projectId)
            waitingListView.adapter = waitingCVAdapter
            waitingListView.layoutManager = LinearLayoutManager(this)
            waitingListView.setHasFixedSize(false)
        } ?: run {
            Toast.makeText(
                this,
                resources.getString(R.string.waiting_projectid_null),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }
}