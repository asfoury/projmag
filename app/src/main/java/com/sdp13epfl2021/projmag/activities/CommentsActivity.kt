package com.sdp13epfl2021.projmag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.MessageListAdapter
import com.sdp13epfl2021.projmag.model.*

class CommentsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val comments = MessagesDatasource().loadMessages()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_comments)
        recyclerView.adapter = MessageListAdapter(this, comments)
        recyclerView.setHasFixedSize(false)
    }
}
