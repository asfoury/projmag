package com.sdp13epfl2021.projmag.database

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
    val description: String
)
