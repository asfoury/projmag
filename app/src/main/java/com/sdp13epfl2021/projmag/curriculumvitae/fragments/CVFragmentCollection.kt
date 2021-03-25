package com.sdp13epfl2021.projmag.curriculumvitae.fragments

class CVFragmentCollection {

    private val intro = IntroFragment()
    private val summary = SummaryFragment()
    private val education = PeriodFragment("Your Education")
    private val job = PeriodFragment("Your Job Experiences")
    private val languages = LanguagesFragment()
    private val submit = SubmitFragment()

    private val frags = listOf(
        intro,
        summary,
        education,
        job,
        languages,
        submit
    ).distinct() // this avoid crashing when changing a fragment for the same one

    operator fun get(pos: Int) = frags[pos]

    fun getItemCount() = frags.size
}