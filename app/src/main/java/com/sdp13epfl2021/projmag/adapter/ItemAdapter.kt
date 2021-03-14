package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.Project

class ItemAdapter(private val context: Context, private val dataset : List<Project>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.project_title)
        val labNameView : TextView = view.findViewById(R.id.lab_name)
        val linearLayoutView : LinearLayout = view.findViewById(R.id.linear_layout_2)


    }


    override fun getItemCount() =  dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
      val adapterLayout = LayoutInflater.from(parent.context)
              .inflate(R.layout.list_project, parent, false)
        return ItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val project = dataset[position]
        // set the project name
        holder.textView.text = project.name.toString()
        // set the lab name
        holder.labNameView.text = project.lab
        // add the tags to the project
        val group = ChipGroup(context)
        for(tag in project.tags) {
            val chipView : Chip = Chip(context)
            chipView.text = tag
            group.addView(chipView)
        }
        holder.linearLayoutView.addView(group)
    }

}