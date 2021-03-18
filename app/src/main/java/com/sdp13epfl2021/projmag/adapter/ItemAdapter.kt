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
import com.sdp13epfl2021.projmag.model.Project

class ItemAdapter(private val context: Context, private val dataset: MutableList<Project>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    val datasetAll: List<Project>

    init {
        datasetAll = ArrayList(dataset)
    }

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
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
        for (tag in project.tags) {
            val chipView: Chip = Chip(context)
            chipView.text = tag
            group.addView(chipView)
        }
        holder.linearLayoutView.addView(group)

        // make the projects pressable
        holder.textView.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, ProjectInformationActivity::class.java)
            var projectString = ""
            projectString += project.name
            intent.putExtra("project", project)
            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
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