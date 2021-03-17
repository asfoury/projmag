package com.sdp13epfl2021.projmag.model

import java.util.*

sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val reason: String) : Result<T>()

class ImmutableProject(
    val id: String?,
    val name: String,
    val labName: String,
    val projectManager: String,
    val description: String,
    val numberStudents: Int,
    val masterProject: Boolean,
    val bachelorSemesterProject: Boolean,
    val masterSemesterProject: Boolean,
    val assignedStudents: List<String>
) {
    companion object {
        const val MAX_NAME_SIZE = 40
        const val MAX_DESCRIPTION_SIZE = 300
        const val MAX_STUDENT_NUMBER = 10
        fun build(
            name: String, labName: String,
            projectManager: String,
            description: String,
            numberStudents: Int,
            masterProject: Boolean,
            bachelorSemesterProject: Boolean,
            masterSemesterProject: Boolean,
            assignedStudents: List<String>
        ): Result<ImmutableProject> {
            return when {
                name.length > MAX_NAME_SIZE -> Failure("name is more than $MAX_NAME_SIZE characters")
                labName.length > MAX_NAME_SIZE -> Failure("lab name is more than $MAX_NAME_SIZE characters")
                projectManager.length > MAX_NAME_SIZE -> Failure("project manager name is more than $MAX_NAME_SIZE characters")
                description.length > MAX_DESCRIPTION_SIZE -> Failure("description  is more than $MAX_DESCRIPTION_SIZE characters")
                numberStudents > MAX_STUDENT_NUMBER -> Failure("max student number for a project is set to $MAX_STUDENT_NUMBER")
                assignedStudents.size > numberStudents -> Failure(
                    "there are ${assignedStudents.size} " +
                            "students assigned but only $numberStudents allowed to work for the project"
                )
                else -> Success(
                    ImmutableProject(
                        null,
                        name,
                        labName,
                        projectManager,
                        description,
                        numberStudents,
                        masterProject,
                        bachelorSemesterProject,
                        masterSemesterProject,
                        assignedStudents
                    )
                )
            }
        }
    }



    /**
     * Function that allows copying of a project and modifying only the fields that you wish to modify
     * returns a project wrapped in a success wrapper, or a failure with the explanation wrapped as a string
     *
     * @param name : name of the project
     * @param labName : name of the lab
     * @param projectManager : name of the project manager
     * @param description : description of the project
     * @param numberStudents : possible number of students that can work on this project
     * @param masterProject : can this project be a master project
     * @param bachelorSemesterProject : can this project be a bachelor semester project
     * @param masterSemesterProject : can this project be a master semester project
     * @param assignedStudents : list of the assigned students to the project
     */
    fun buildCopy(
        name: String = this.name,
        labName: String = this.labName,
        projectManager: String = this.projectManager,
        description: String = this.description,
        numberStudents: Int = this.numberStudents,
        masterProject: Boolean = this.masterProject,
        bachelorSemesterProject: Boolean = this.bachelorSemesterProject,
        masterSemesterProject: Boolean = this.masterSemesterProject,
        assignedStudents: List<String> = this.assignedStudents
    ) = build(
        name, labName, projectManager, description, numberStudents,
        masterProject, bachelorSemesterProject, masterSemesterProject, assignedStudents
    )

    /**
     * A map of the members with their name
     */
    fun toMapString(): Map<String, Any> = hashMapOf(
        "name" to name,
        "name-search" to name.toLowerCase(Locale.ROOT).split(" "),
        "labName" to labName,
        "labName-search" to labName.toLowerCase(Locale.ROOT),
        "projectManager" to projectManager,
        "numberStudents" to numberStudents,
        "assignedStudents" to assignedStudents,
        "masterProject" to masterProject,
        "bachelorSemesterProject" to bachelorSemesterProject,
        "description" to description
    )


}