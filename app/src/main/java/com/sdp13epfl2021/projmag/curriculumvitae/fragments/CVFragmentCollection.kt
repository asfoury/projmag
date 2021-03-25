package com.sdp13epfl2021.projmag.curriculumvitae.fragments

class CVFragmentCollection {
    companion object {
        private const val EDUCATION_TITLE = "Your Education"
        private const val JOB_TITLE = "Your Job Experience"

    }

    private val intro = IntroFragment()
    private val summary = SummaryFragment()
    private val education = PeriodFragment.getInstance(EDUCATION_TITLE)
    private val job = PeriodFragment.getInstance(JOB_TITLE)
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

    operator fun get(pos: Int) = frags[pos]

    fun getItemCount() = frags.size
}