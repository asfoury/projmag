package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.blue
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.listenerClass.RecyclerItemClickListenr
import com.sdp13epfl2021.projmag.adapter.TagAdapter
import com.sdp13epfl2021.projmag.model.Tag
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import java.io.Serializable


class TagsSelectorActivity : AppCompatActivity() {
    private val selectedTags : MutableSet<Tag> = mutableSetOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_selector)

        val manager = TagsBaseManager()
        val tagsDataset = manager.getAllTags().toList()
        val tagRecyclerView = findViewById<RecyclerView>(R.id.recycler_tag_view)
        val saveButton = findViewById<Button>(R.id.DoneTagButton)
        tagRecyclerView.adapter = TagAdapter(this, tagsDataset)
        tagRecyclerView.setHasFixedSize(true)


        tagRecyclerView.addOnItemTouchListener(
            RecyclerItemClickListenr(
                this,
                tagRecyclerView,
                object : RecyclerItemClickListenr.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        if(selectedTags.contains(tagsDataset[position])) {
                            tagRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.alpha = 0.5f
                            selectedTags.remove(tagsDataset[position])
                            //need to add color change somehow
                        }
                        else{
                            tagRecyclerView.findViewHolderForLayoutPosition(position)?.itemView?.alpha = 1f
                            selectedTags.add(tagsDataset[position])

                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {

                    }
                })
        )

        saveButton.setOnClickListener{
            val returnIntent = Intent()
            val tagsManager = TagsBaseManager()
            val tags  = tagsManager.tagsListToStringList(selectedTags).toTypedArray()

            //This should work because String is inherently serializable but I could get crashes
            returnIntent.putExtra("tagsList",  tags as Serializable)
            setResult(RESULT_OK, returnIntent)
            finish()
        }


    }


    fun allSelectedTags() : MutableSet<Tag>{
        return selectedTags
    }


}