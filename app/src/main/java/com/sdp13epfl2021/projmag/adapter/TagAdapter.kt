package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.Tag
/*
class TagAdapter(private val context : Context, val tags : List<Tag>)
    : RecyclerView.Adapter<ProjectAdapter.ItemViewHolder>() {

    class TagViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.tag)
    }



     fun CreateViewHolder(parent: ViewGroup, viewType: Int){
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_item, parent, false)

    }

    override fun onBindViewHolder(holder: ProjectAdapter.ItemViewHolder, position: Int) {
        val item = tags[position]
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int = tags.size
}*/