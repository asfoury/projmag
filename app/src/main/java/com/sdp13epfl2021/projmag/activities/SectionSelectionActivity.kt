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
import com.sdp13epfl2021.projmag.adapter.SectionAdapter
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import java.io.Serializable

/**
 * Activity in which to select the sections of a project upon its creation
 */
class SectionSelectionActivity : AppCompatActivity() {
    private val chosenSections: MutableSet<String> = mutableSetOf()
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
     * Handling of what happens when you click on sections in the UI
     *
     * @param sectionRecyclerView : the section recycler view
     * @param sectionDataset : the dataset of sections
     */
    private fun handleListeningOnElementsOfSectionRecyclerView(
        sectionRecyclerView: RecyclerView,
        sectionDataset: List<String>
    ) {
        sectionRecyclerView.addOnItemTouchListener(
            MakeRecyclerListItemsClickListenable(
                this,
                sectionRecyclerView,
                object : MakeRecyclerListItemsClickListenable.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        val holder =
                            sectionRecyclerView.findViewHolderForLayoutPosition(position) as SectionAdapter.SectionViewHolder
                        if (chosenSections.contains(sectionDataset[position])) {
                            holder.textView.setBackgroundColor(Color.RED)
                            chosenSections.remove(sectionDataset[position])
                        } else {
                            holder.textView.setBackgroundColor(Color.GREEN)
                            chosenSections.add(sectionDataset[position])

                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {

                    }
                })
        )
    }

    /**
     * Handling of the save button inside of the section selection activity
     *
     * @param saveButton
     */
    private fun saveButtonHandling(saveButton: Button) {
        saveButton.setOnClickListener {
            val returnIntent = Intent()
            val sections = chosenSections.toTypedArray()


            returnIntent.putExtra(MainActivity.sectionsList, sections as Serializable)
            setResult(AppCompatActivity.RESULT_OK, returnIntent)
            finish()
        }
    }
}



