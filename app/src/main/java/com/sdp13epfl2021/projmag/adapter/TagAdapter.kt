package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Tag

class TagAdapter(private val context : Context, val tags : List<Tag>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class TagViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_item, parent, false)
        return ItemAdapter.ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        val item = tags[position]
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int = tags.size
}