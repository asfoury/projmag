package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.adapter.SectionAdapter
import com.sdp13epfl2021.projmag.adapter.TagAdapter
import com.sdp13epfl2021.projmag.model.SectionBaseManager
import com.sdp13epfl2021.projmag.model.TagsBaseManager
import java.io.Serializable

class SectionSelectionActivity : AppCompatActivity() {
    private val  chosenSections : List<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_selection)



        val manager = SectionBaseManager()
        val sectionDatabaset = manager.sectionList()
        val sectionRecyclerView = findViewById<RecyclerView>(R.id.recycler_section_view)
        val saveButton = findViewById<Button>(R.id.doneSectionButton)
        sectionRecyclerView.adapter = SectionAdapter(this, sectionDatabaset)
        sectionRecyclerView.setHasFixedSize(true)


        saveButtonHandling(saveButton)




    }


    private fun saveButtonHandling(saveButton: Button){
        saveButton.setOnClickListener{
            val returnIntent = Intent()
            val sections  = chosenSections.toTypedArray()

            //This should work because String is inherently serializable but I could get crashes
            returnIntent.putExtra("sectionsList",  sections as Serializable)
            setResult(AppCompatActivity.RESULT_OK, returnIntent)
            finish()
        }
    }
}



