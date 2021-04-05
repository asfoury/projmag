package com.sdp13epfl2021.projmag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.TagAdapter
import com.sdp13epfl2021.projmag.model.TagsBaseManager

class tagsSelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_selector)

        val manager = TagsBaseManager()
        val tagsDataset = manager.getAllTags()
        val tagRecyclerView = findViewById<RecyclerView>(R.id.recycler_tag_view)
        tagRecyclerView.adapter = TagAdapter(this, tagsDataset.toList())
        tagRecyclerView.setHasFixedSize(true)
    }
}