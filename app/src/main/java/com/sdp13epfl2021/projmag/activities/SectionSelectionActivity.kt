package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.listenerClass.MakeRecyclerListItemsClickListenable
import com.sdp13epfl2021.projmag.adapter.SectionAdapter
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import java.io.Serializable

class SectionSelectionActivity : AppCompatActivity() {
    private val  chosenSections : MutableSet<String> = mutableSetOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_selection)




        val sectionDataset = SectionBaseManager.sectionList()
        val sectionRecyclerView = findViewById<RecyclerView>(R.id.recycler_section_view)
        val saveButton = findViewById<Button>(R.id.doneSectionButton)
        sectionRecyclerView.adapter = SectionAdapter(this, sectionDataset)
        sectionRecyclerView.setHasFixedSize(true)

        handleListeningOnElementsOfSectionRecyclerView(sectionRecyclerView, sectionDataset)
        saveButtonHandling(saveButton)




    }
    /**
     * Function that han dles the click and long click of elements of the tag recycler view
     *
     * @param tagRecyclerView : the tag recycler view
     * @param tagsDataset : the dataset of tags
     */
    private fun handleListeningOnElementsOfSectionRecyclerView(sectionRecyclerView: RecyclerView, sectionDataset:List<String>){
        sectionRecyclerView.addOnItemTouchListener(
            MakeRecyclerListItemsClickListenable(
                this,
                sectionRecyclerView,
                object : MakeRecyclerListItemsClickListenable.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        val holder = sectionRecyclerView.findViewHolderForLayoutPosition(position) as SectionAdapter.SectionViewHolder
                        if(chosenSections.contains(sectionDataset[position])) {
                            holder.textView.setBackgroundColor(Color.RED)
                            chosenSections.remove(sectionDataset[position])
                        }
                        else{
                            holder.textView.setBackgroundColor(Color.GREEN)
                            chosenSections.add(sectionDataset[position])

                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {

                    }
                })
        )
    }

    private fun saveButtonHandling(saveButton: Button){
        saveButton.setOnClickListener{
            val returnIntent = Intent()
            val sections  = chosenSections.toTypedArray()


            returnIntent.putExtra(MainActivity.sectionsList,  sections as Serializable)
            setResult(AppCompatActivity.RESULT_OK, returnIntent)
            finish()
        }
    }
}



