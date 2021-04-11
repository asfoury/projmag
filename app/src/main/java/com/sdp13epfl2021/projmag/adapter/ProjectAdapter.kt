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

class ProjectAdapter(private val context: Context, private val utils: Utils, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<ProjectAdapter.ItemViewHolder>(), Filterable {

    var datasetAll: List<ImmutableProject> = utils.projectsDatabase.getAllProjects()
    val dataset: MutableList<ImmutableProject> = datasetAll.toMutableList()

    init {
        dataset.sortBy{ project -> project.isTaken}
        utils.projectsDatabase.addProjectsChangeListener { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> addProject(change.project)
                ProjectChange.Type.MODIFIED -> addProject(change.project)
                ProjectChange.Type.REMOVED -> removeProject(change.project)
            }
            notifyDataSetChanged()
        }
    }

    @Synchronized
    private fun addProject(project: ImmutableProject) {
        val oldProject: ImmutableProject? = dataset.find { p -> p.id == project.id }
        if (oldProject == null) {
            datasetAll = datasetAll + project
            dataset.add(project)
        } else {
            datasetAll = datasetAll - oldProject + project
            val index = dataset.indexOf(oldProject)
            if (index >= 0 && index < dataset.size) {
                dataset[index] = project
            }
        }
        greyOut()
        dataset.sortBy{ p -> p.isTaken}
    }

    @Synchronized
    private fun removeProject(project: ImmutableProject) {
        datasetAll = datasetAll - project
        dataset.remove(project)
    }

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.project_title)
        val labNameView: TextView = view.findViewById(R.id.lab_name)
        val linearLayoutView: LinearLayout = view.findViewById(R.id.linear_layout_2)
        val chipGroupView : ChipGroup = view.findViewById(R.id.chip_group)
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
        holder.textView.text = project.name
        // set the lab name
        holder.labNameView.text = project.lab

        // remove all tags to keep them from being duplicated
        holder.chipGroupView.removeAllViews()

        // add the tags to the project
            for (tag in project.tags) {
                val chipView: Chip = Chip(context)
                chipView.text = tag
                holder.chipGroupView.addView(chipView)
            }

        if (dataset[position].isTaken) {
            holder.view.alpha = 0.5f
        } else {
            holder.view.alpha = 1f
        }

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

    fun greyOut() {
        for (project in dataset) {
            val i = dataset.indexOf(project)
            if (project.isTaken) {
                recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha = 0.5f
            } else {
                recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha = 1f
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<ImmutableProject>()
                val search = constraint.toString()
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(datasetAll)
                } else {
                    for (project in datasetAll) if (project.name.toLowerCase().contains(search.toLowerCase())) filteredList.add(project)
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataset.clear()
                dataset.addAll(performFiltering(constraint).values as Collection<ImmutableProject>)
                dataset.sortBy{ project -> project.isTaken}
                greyOut()
                notifyDataSetChanged()
            }
        }
    }

}