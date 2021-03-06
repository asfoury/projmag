package com.sdp13epfl2021.projmag.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.CommentsActivity
import com.sdp13epfl2021.projmag.activities.ProjectInformationActivity
import com.sdp13epfl2021.projmag.database.ProjectChange
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ProjectFilter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter for project to recycler view. Allows projects to be displayed in project list.
 */
class ProjectAdapter(
    private val activity: Activity,
    projectDB: ProjectDatabase,
    private val recyclerView: RecyclerView,
    private val fromLink: Boolean,
    private var projectIdLink: String
) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>(), Filterable {

    companion object ItemAdapterCompanion {
        private const val projectString = "project"
    }

    var datasetAll: List<ImmutableProject> = emptyList()
    val dataset: MutableList<ImmutableProject> = datasetAll.toMutableList()

    /**
     * Sort the list of projects, first are the projects not taken, sorted with newest date first.
     * If the activity is created with a link, it will put it at the top.
     */
    private fun sortDataset() {
        dataset.sortWith { a, b ->
            if (a.isTaken == b.isTaken) {
                b.creationDate.compareTo(a.creationDate)
            } else {
                a.isTaken.compareTo(b.isTaken)
            }
        }
        if (fromLink) {
            dataset.sortByDescending { project -> projectIdLink == project.id }
        }
    }

    /**
     * Make dataset listen for changes to the projects and fetches
     * all projects from database.
     */
    init {
        projectDB.addProjectsChangeListener { change ->
            when (change.type) {
                ProjectChange.Type.ADDED -> addProject(change.project)
                ProjectChange.Type.MODIFIED -> addProject(change.project)
                ProjectChange.Type.REMOVED -> removeProject(change.project)
            }
            activity.runOnUiThread { notifyDataSetChanged() }
        }

        projectDB.getAllProjects({ it.forEach(this::addProject) }, {})
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
        sortDataset()
        activity.runOnUiThread { notifyDataSetChanged() }
    }

    @Synchronized
    private fun removeProject(project: ImmutableProject) {
        datasetAll = datasetAll - project
        dataset.remove(project)
    }

    /**
     * Holder of project fields to display in list.
     */
    class ProjectViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.project_title)
        val labNameView: TextView = view.findViewById(R.id.lab_name)
        val chipGroupView: ChipGroup = view.findViewById(R.id.chip_group)
        val commentButton: ImageButton = view.findViewById(R.id.projects_comments_button)
    }


    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_project, parent, false)
        return ProjectViewHolder(adapterLayout)
    }

    private fun openProject(holder: ProjectViewHolder, project: ImmutableProject) {
        val context = holder.view.context
        val intent = Intent(context, ProjectInformationActivity::class.java)
        intent.putExtra(projectString, project as Parcelable)
        context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = dataset[position]
        // set the project name
        holder.textView.text = project.name
        // set the lab name
        holder.labNameView.text = project.lab

        // remove all tags to keep them from being duplicated
        holder.chipGroupView.removeAllViews()

        // go to the comments activity when comments button is pressed
        holder.commentButton.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(MainActivity.projectIdString, project.id)
            context.startActivity(intent)
        }

        if (dataset[position].isTaken) {
            holder.view.alpha = 0.5f
        } else {
            holder.view.alpha = 1f
        }

        //put the tags and the sections :
        tagAndSectionsChipViewSetup(project, holder)

        // make the projects pressable
        holder.view.setOnClickListener {
            openProject(holder, project)
        }

        if (projectIdLink == project.id) {
            openProject(holder, project)
            projectIdLink = ""
        }

    }

    private fun greyOut() {
        for (project in dataset) {
            val i = dataset.indexOf(project)
            if (project.isTaken) {
                recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha = 0.5f
            } else {
                recyclerView.findViewHolderForLayoutPosition(i)?.itemView?.alpha = 1f
            }
        }
    }


    private fun tagAndSectionsChipViewSetup(project: ImmutableProject, holder: ProjectViewHolder) {
        val green = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.light_green))
        val teal = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.teal_700))
        chipAdding(project.tags, holder, green)
        chipAdding(project.allowedSections, holder, teal)
    }

    private fun chipAdding(list: List<String>, holder: ProjectViewHolder, color: ColorStateList) {
        for (text in list) {
            val chipView: Chip = Chip(activity)
            chipView.text = text
            chipView.chipBackgroundColor = color
            holder.chipGroupView.addView(chipView)
        }
    }

    /**
     * Return a list Filter for this Adapter with the given ProjectFilter
     *
     * @param pf the ProjectFilter
     * @return a Filter for this adapter
     */
    fun getFilter(pf: ProjectFilter): Filter = ProjectListFilter(pf)


    override fun getFilter(): Filter {
        return ProjectListFilter()
    }

    /**
     * Filter for project list, used when searching for a project by name.
     */
    private inner class ProjectListFilter(val projectFilter: ProjectFilter = ProjectFilter()) :
        Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<ImmutableProject>()
            val search = constraint.toString()
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(datasetAll)
            } else {
                for (project in datasetAll) {
                    if (project.name.toLowerCase(Locale.ROOT)
                            .contains(search.toLowerCase(Locale.ROOT))
                    ) {
                        filteredList.add(project)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList.filter { projectFilter(it) }
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataset.clear()
            dataset.addAll(performFiltering(constraint).values as Collection<ImmutableProject>)
            sortDataset()
            greyOut()
            notifyDataSetChanged()
        }
    }

}
