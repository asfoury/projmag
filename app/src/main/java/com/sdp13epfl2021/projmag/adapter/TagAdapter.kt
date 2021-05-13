package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.Tag

/**
 * Adapter of tags to recycler view to display them in list.
 */
class TagAdapter(private val context: Context, private val tags: List<Tag>) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    /**
     * Holder of tags text to display in list.
     */
    class TagViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        //tag item is not yet defined
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.list_tag, parent, false)
        return TagViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.textView.text = tag.name


    }


    override fun getItemCount(): Int = tags.size

}