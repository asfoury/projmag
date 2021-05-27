package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.SkillDescription
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae.SkillDescription.SkillLevel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A Fragment, where the user should input various skills
 */
@AndroidEntryPoint
class SkillsFragment : Fragment() {

    private val skillList: MutableList<SkillDescription> = mutableListOf()

    private val skillListAdapter = CVListAdapter(skillList, ::removeFromSkillList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cv_skills, container, false)
        val spinner = view?.findViewById<Spinner>(R.id.cv_skills_spinner)
        val values = SkillLevel.values()
        spinner?.adapter = activity?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.spinner_layout_cv,
                values
            )
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.cv_skills_recyclerview)
        recyclerView?.adapter = skillListAdapter

        val addButton = view?.findViewById<Button>(R.id.cv_skills_add)
        addButton?.setOnClickListener(::buttonIsClicked)

        return view
    }


    /**
     * Return the data given by the user
     *
     * @return the result of this fragment as a list of skills
     */
    fun get() = skillList


    private fun addToSkillList(skill: SkillDescription) {
        if (skill.isValid()) {
            skillList.addIfNotExisting(skill)
            skillList.sortBy { -(it.skillLevel.ordinal) }
            skillListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(activity, "Invalid data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun removeFromSkillList(skill: SkillDescription) {
        skillList.remove(skill)
        skillListAdapter.notifyDataSetChanged()
    }


    private fun buttonIsClicked(v: View) {
        val name = view?.findViewById<EditText>(R.id.cv_skills_name)?.text?.toString()
        val level = view?.findViewById<Spinner>(R.id.cv_skills_spinner)?.selectedItem as? SkillLevel

        if (name != null && level != null) {
            addToSkillList(SkillDescription(name, level))
        } else {
            Toast.makeText(activity, "Couldn't get data", Toast.LENGTH_SHORT).show()
        }
    }
}