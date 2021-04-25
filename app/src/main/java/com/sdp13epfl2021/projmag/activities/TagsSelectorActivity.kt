
package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.listenerClass.MakeRecyclerListItemsClickListenable
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

        //handling of the buttons and the app
        handleListeningOnElementsOfTagRecyclerView(tagRecyclerView, tagsDataset )
        saveButtonHandling(saveButton)




    }

    /**
     * Function that handles the click and long click of elements of the tag recycler view
     *
     * @param tagRecyclerView : the tag recycler view
     * @param tagsDataset : the dataset of tags
     */
    private fun handleListeningOnElementsOfTagRecyclerView(tagRecyclerView: RecyclerView, tagsDataset: List<Tag>){
        tagRecyclerView.addOnItemTouchListener(
            MakeRecyclerListItemsClickListenable(
                this,
                tagRecyclerView,
                object : MakeRecyclerListItemsClickListenable.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        val holder = tagRecyclerView.findViewHolderForLayoutPosition(position) as TagAdapter.TagViewHolder
                        if(selectedTags.contains(tagsDataset[position])) {
                            holder.textView.setBackgroundColor(Color.RED)
                            selectedTags.remove(tagsDataset[position])
                        }
                        else{
                            holder.textView.setBackgroundColor(Color.GREEN)
                            selectedTags.add(tagsDataset[position])

                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {

                    }
                })
        )
    }

    /**
     * Function responsible for handling the save button behaviour
     * returns the list of selected tags to the previous activity for project creation
     * @param saveButton : the button the user presses to save a project
     */
    private fun saveButtonHandling(saveButton: Button){
        saveButton.setOnClickListener{
            val returnIntent = Intent()
            val tagsManager = TagsBaseManager()
            val tags  = tagsManager.tagsListToStringList(selectedTags).toTypedArray()

            //This should work because String is inherently serializable but I could get crashes
            returnIntent.putExtra(MainActivity.tagsList,  tags as Serializable)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
    fun allSelectedTags() : MutableSet<Tag>{
        return selectedTags
    }

    /*
    private fun colorPreviouslySelectedSections(previouslySelectedSections : List<String>){
        for(section in previouslySelectedSections){

            val holder = tagRecyclerView.findViewHolderForLayoutPosition(position)
            holder.textView.setTextColor(Color.GREEN)
        }
    }*/

}
