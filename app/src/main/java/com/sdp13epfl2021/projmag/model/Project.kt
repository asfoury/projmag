package com.sdp13epfl2021.projmag.model

data class Project(
        val name : String,
        val lab : String,
        val teacher : String,
        val projectTA : String?,
        val numberOfParticipants : Int,
        val assignedStudents : List<String>,
        val isMasterProject : Boolean,
        val isSemesterProject : Boolean,
        val tags : List<String>,
        val isTaken : Boolean,
        val description : String
        )