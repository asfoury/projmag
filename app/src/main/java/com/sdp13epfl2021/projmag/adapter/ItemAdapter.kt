package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.model.Project

class ItemAdapter(private val context: Context, private val dataset: MutableList<Project>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    val datasetAll: List<Project>

    init {
        datasetAll = ArrayList(dataset)
    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.project_title)
        val labNameView: TextView = view.findViewById(R.id.lab_name)
        val linearLayoutView: LinearLayout = view.findViewById(R.id.linear_layout_2)
    }


    override fun getItemCount() = dataset.size

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
        // give the group that contains the tags an id so that we can check to see if the view already has id so they are not redrawn
        group.id = R.id.mygroup_tag

        // add the tags only if they are not present
        if (holder.linearLayoutView.findViewById<ChipGroup>(R.id.mygroup_tag) == null) {
            for (tag in project.tags) {
                val chipView: Chip = Chip(context)
                chipView.text = tag
                group.addView(chipView)
            }
            holder.linearLayoutView.addView(group)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Project>()
                val search = constraint.toString()
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(datasetAll)
                } else {
                    for (project in datasetAll) {
                        if (project.name.toLowerCase().contains(search.toLowerCase())) {
                            filteredList.add(project)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataset.clear()
                dataset.addAll(performFiltering(constraint).values as Collection<Project>)
                notifyDataSetChanged()
            }
        }
    }

}