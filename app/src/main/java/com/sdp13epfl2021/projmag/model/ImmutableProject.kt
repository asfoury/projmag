package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val reason: String) : Result<T>()

@Parcelize
 data class ImmutableProject  constructor(
    val id: String,
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
) : Parcelable {
    companion object {
         const val MAX_PROJECT_NAME_SIZE = 120
         const val MAX_NAME_SIZE = 40
         const val MAX_DESCRIPTION_SIZE = 4000
        private const val MAX_STUDENT_NUMBER = 10
        fun build(
            id: String,
            name: String,
            lab: String,
            teacher: String,
            TA: String,
            nbParticipant: Int,
            assigned: List<String>,
            masterProject: Boolean,
            bachelorProject: Boolean,
            tags: List<String>,
            isTaken: Boolean,
            description: String
        ): Result<ImmutableProject> {
            return when {
                name.length > MAX_PROJECT_NAME_SIZE -> Failure("name is more than $MAX_PROJECT_NAME_SIZE characters")
                lab.length > MAX_NAME_SIZE -> Failure("lab name is more than $MAX_NAME_SIZE characters")
                TA.length > MAX_NAME_SIZE -> Failure("project manager name is more than $MAX_NAME_SIZE characters")
                teacher.length > MAX_NAME_SIZE -> Failure("teacher name is more than $MAX_NAME_SIZE characters")
                description.length > MAX_DESCRIPTION_SIZE -> Failure("description  is more than $MAX_DESCRIPTION_SIZE characters")
                nbParticipant > MAX_STUDENT_NUMBER -> Failure("max student number for a project is set to $MAX_STUDENT_NUMBER")
                assigned.size > nbParticipant -> Failure(
                    "there are ${assigned.size} " +
                            "students currently assigned but only $nbParticipant allowed to work for the project"
                )
                else -> Success(
                    ImmutableProject(
                        id,
                        name,
                        lab,
                        teacher,
                        TA,
                        nbParticipant,
                        assigned,
                        masterProject,
                        bachelorProject,
                        tags,
                        isTaken,
                        description,
                    )
                )
            }
        }
    }


    /**
     * Function that allows copying of a project and modifying only the fields that you wish to modify
     * returns a project wrapped in a success wrapper, or a failure with the explanation wrapped as a string
     *
     * @param id : project id from firebase
     * @param name : name of the project
     * @param lab : lab of the project
     * @param teacher : person responsible for the lab
     * @param TA : person responsible for the project
     * @param nbParticipant : number of participants
     * @param assigned : list of the assigned students
     * @param masterProject : is this project available for a 12 credits master project
     * @param bachelorProject : is this project available as a bachelor semester project
     * @param tags : tags associated to the project
     * @param isTaken : Is this project already taken
     * @param description : description of the project
     */
    fun buildCopy(
        id: String = this.id,
        name: String = this.name,
        lab: String = this.lab,
        teacher: String = this.teacher,
        TA: String = this.TA,
        nbParticipant: Int = this.nbParticipant,
        assigned: List<String> = this.assigned,
        masterProject: Boolean = this.masterProject,
        bachelorProject: Boolean = this.bachelorProject,
        tags: List<String> = this.tags,
        isTaken: Boolean = this.isTaken,
        description: String = this.description
    ) = build(
        id, name, lab, teacher, TA, nbParticipant, assigned, masterProject, bachelorProject,
        tags, isTaken, description,
    )

    /**
     *  Give a Map<String,Any> the maps name of members to their values.
     */
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