package com.sdp13epfl2021.projmag.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae

class CVAdapter(
    activity: Context,
    val cv: CurriculumVitae
) : RecyclerView.Adapter<CVAdapter.CVHolder>() {

    private val res: Resources = activity.resources

    private val educationStart: Int = 0
    private val jobStart: Int = educationStart + cv.education.size
    private val languageStart: Int = jobStart + cv.jobExperience.size
    private val skillStart: Int = languageStart + cv.languages.size
    private val totalSize: Int =
        cv.education.size +
                cv.jobExperience.size +
                cv.languages.size +
                cv.skills.size

    private val educationRange: IntRange = educationStart until jobStart
    private val jobRange: IntRange = jobStart until languageStart
    private val languageRange: IntRange = languageStart until skillStart
    private val skillRange: IntRange = skillStart until totalSize


    /**
     * Class containing fields to show in recycler view display of information from CurriculumVitae
     * Instead of having different type of holder for periods, languages and skills,
     * this class offers 3 different text view.
     */
    class CVHolder(view: View) : RecyclerView.ViewHolder(view) {
        val line0TextView: TextView = view.findViewById(R.id.cv_display_item_line_0)
        val line1TextView: TextView = view.findViewById(R.id.cv_display_item_line_1)
        val line2TextView: TextView = view.findViewById(R.id.cv_display_item_line_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CVHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_display_item, parent, false)
        return CVHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CVHolder, position: Int) {
        val texts: Triple<String, String, String> = when {
            educationRange.contains(position) -> periodTexts(cv.education[position - educationStart])
            jobRange.contains(position) -> periodTexts(cv.jobExperience[position - jobStart])
            languageRange.contains(position) -> {
                val language = cv.languages[position - languageStart]
                Triple(
                    res.getString(R.string.cv_display_item_language, language.language),
                    res.getString(R.string.cv_display_item_level, language.level),
                    ""
                )
            }
            skillRange.contains(position) -> {
                val skill = cv.skills[position - skillStart]
                Triple(
                    res.getString(R.string.cv_display_item_skill, skill.name),
                    res.getString(R.string.cv_display_item_level, skill.skillLevel),
                    ""
                )
            }
            else -> Triple("", "", "")
        }
        setTexts(holder, texts)
    }

    private fun setTexts(holder: CVHolder, texts: Triple<String, String, String>) {
        holder.line0TextView.text = texts.first
        holder.line1TextView.text = texts.second
        holder.line2TextView.text = texts.third
    }

    private fun periodTexts(period: CurriculumVitae.PeriodDescription): Triple<String, String, String> {
        return Triple(
            period.toString(),
            res.getString(R.string.cv_display_item_location, period.location),
            period.description
        )
    }

    override fun getItemCount(): Int = totalSize

}