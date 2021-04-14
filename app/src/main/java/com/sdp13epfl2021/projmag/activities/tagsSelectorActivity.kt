package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.Form
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.listenerClass.RecyclerItemClickListenr
import com.sdp13epfl2021.projmag.adapter.TagAdapter
import com.sdp13epfl2021.projmag.model.Tag

import com.sdp13epfl2021.projmag.model.TagsBaseManager

class tagsSelectorActivity : AppCompatActivity() {
    private val selectedTags : MutableSet<Tag> = mutableSetOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_selector)

        val manager = TagsBaseManager()
        val tagsDataset = manager.getAllTags().toList()
        val tagRecyclerView = findViewById<RecyclerView>(R.id.recycler_tag_view)
        tagRecyclerView.adapter = TagAdapter(this, tagsDataset)
        tagRecyclerView.setHasFixedSize(true)


        tagRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListenr(this, tagRecyclerView, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                selectedTags.add(tagsDataset[position])

            }
            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))

    }
    fun allSelectedTags() : MutableSet<Tag>{
        return selectedTags
    }
}