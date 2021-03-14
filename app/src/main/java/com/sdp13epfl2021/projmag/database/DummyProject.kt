package com.sdp13epfl2021.projmag.database

import java.util.*

data class DummyProject(
    val name: String,
    val lab: String,
    val teacher: String,
    val TA: String,
    val nbParticipant: Int,
    val assigned: List<String>,
    val masterProject: Boolean,
    val bachelorProject: Boolean,
    val tags: List<String>,
    val isTaken: Boolean,
    val description: String) {

    fun toMapString() = hashMapOf(
        "name" to name,
        "name-search" to name.toLowerCase(Locale.ROOT).split(" "),
        "lab" to lab,
        "lab-search" to lab.toLowerCase(Locale.ROOT),
        "teacher" to teacher,
        "teacher-search" to teacher.toLowerCase(Locale.ROOT).split(" "),
        "TA" to TA,
        "TA-search" to TA.toLowerCase(Locale.ROOT),
        "nbParticipant" to nbParticipant,
        "assigned" to assigned,
        "masterProject" to masterProject,
        "bachelorProject" to bachelorProject,
        "tags" to tags,
        "tags-search" to tags.map { it.toLowerCase(Locale.ROOT) },
        "isTaken" to isTaken,
        "description" to description
    )
}

