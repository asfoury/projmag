package com.sdp13epfl2021.projmag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.ImmutableProject

class OwnProjectAdapter (private val ownListProjet: List<ImmutableProject>) : RecyclerView.Adapter<OwnProjectAdapter.ViewHolder>()  {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.project_title)
        val labNameView: TextView = view.findViewById(R.id.lab_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_project, parent, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = ownListProjet[position]
        holder.textView.text = project.name
        holder.labNameView.text = project.lab
    }

    override fun getItemCount(): Int {
        return ownListProjet.size
    }
}