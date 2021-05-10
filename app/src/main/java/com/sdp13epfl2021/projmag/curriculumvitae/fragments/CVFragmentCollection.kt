package com.sdp13epfl2021.projmag.curriculumvitae.fragments

import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae

/**
 * A collection of all fragment used inside the CV creation activity
 */
class CVFragmentCollection {

    private val educationTitle = "Your Education"
    private val jobTitle = "Your Job Experience"

    private val intro = IntroFragment()
    private val summary = SummaryFragment()
    private val education = PeriodFragment.getInstance(educationTitle)
    private val job = PeriodFragment.getInstance(jobTitle)
    private val languages = LanguagesFragment()
    private val skills = SkillsFragment()
    private val submit = SubmitFragment()

    private val frags = listOf(
        intro,
        summary,
        education,
        job,
        languages,
        skills,
        submit
    ).distinct() // this avoid crashing when changing a fragment for the same one

    /**
     * Get the n'th fragment
     *
     * @param n the index of fragment to get
     * @return the fragment at index `n`
     */
    operator fun get(n: Int) = frags[n]

    /**
     * Add a callback function that would be used when submitting the CV
     */
    fun addCallbackOnSubmission(callback: () -> Unit) = submit.addCallback(callback)

    /**
     * build the `CurriculumVitea` from the value of each fragment
     */
    fun buildCV() =
        CurriculumVitae(
            summary = summary.get(),
            education = education.get(),
            jobExperience = job.get(),
            languages = languages.get(),
            skills = skills.get()
        )

    /**
     * get the number of fragments in the collection
     */
    fun getItemCount() = frags.size
}