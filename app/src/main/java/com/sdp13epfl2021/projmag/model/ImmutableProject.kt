    package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import com.sdp13epfl2021.projmag.database.ProjectId
import com.sdp13epfl2021.projmag.model.ImmutableProject.Companion.FieldNames.toSearchName
import kotlinx.parcelize.Parcelize
import java.lang.ClassCastException
import java.lang.NullPointerException
import java.util.*

    sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val reason: String) : Result<T>()

@Parcelize
data class ImmutableProject(
    val id: String,
    val name: String,
    val lab: String,
    val authorId: String,
    val teacher: String,
    val TA: String,
    val nbParticipant: Int,
    val assigned: List<String>,
    val masterProject: Boolean,
    val bachelorProject: Boolean,
    val tags: List<String>,
    val isTaken: Boolean,
    val description: String,
    val videoURI: List<String> = listOf(),
    val allowedSections: List<String> = listOf(),
) : Parcelable {
    companion object {
        public object FieldNames {
            fun String.toSearchName(): String = "${this}-search"
            const val NAME = "name"
            const val LAB = "lab"
            const val AUTHOR_ID = "authorID"
            const val TEACHER = "teacher"
            const val TA = "TA"
            const val NB_PARTICIPANT = "nbParticipant"
            const val ASSIGNED = "assigned"
            const val MASTER_PROJECT = "masterProject"
            const val BACHELOR_PROJECT = "bachelorProject"
            const val TAGS = "tags"
            const val IS_TAKEN = "isTaken"
            const val DESCRIPTION = "description"
            const val VIDEO_URI = "videoURI"
            const val ALLOWED_SECTIONS = "allowedSections"
        }

        const val MAX_PROJECT_NAME_SIZE = 120
        const val MAX_NAME_SIZE = 40
        const val MAX_DESCRIPTION_SIZE = 4000
        private const val MAX_STUDENT_NUMBER = 10

        fun build(
            id: String,
            name: String,
            lab: String,
            authorId: String,
            teacher: String,
            TA: String,
            nbParticipant: Int,
            assigned: List<String>,
            masterProject: Boolean,
            bachelorProject: Boolean,
            tags: List<String>,
            isTaken: Boolean,
            description: String,
            videoURI: List<String> = listOf(),
            allowedSections: List<String> = listOf()
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
                //!sectionsManager.isListValid(allowedSections) -> Failure("a section in the section list doesn't exist $allowedSections")
                 //!tagsManager.isListOfStringsValidTags(tags) -> Failure("a tag in the tag list doesn't exist $tags")

                else -> Success(
                    ImmutableProject(
                        id,
                        name,
                        lab,
                        authorId,
                        teacher,
                        TA,
                        nbParticipant,
                        assigned,
                        masterProject,
                        bachelorProject,
                        tags,
                        isTaken,
                        description,
                        videoURI,
                        allowedSections
                    )
                )
            }
        }

        /**
         * Build a project from the given map
         *
         * @param map : a map containing all fields of the project
         * @param projectId : the id of the project
         *
         * @return an ImmutableProject if the build succeed, null otherwise
         */
        @Suppress("UNCHECKED_CAST")
        fun buildFromMap(map: Map<String, Any?>, projectId: ProjectId): ImmutableProject? {
            try {
                val result = build(
                    id = projectId,
                    name = map[FieldNames.NAME] as String,
                    lab = map[FieldNames.LAB] as String,
                    authorId = map[FieldNames.AUTHOR_ID] as String,
                    teacher = map[FieldNames.TEACHER] as String,
                    TA = map[FieldNames.TA] as String,
                    nbParticipant = (map[FieldNames.NB_PARTICIPANT] as Number).toInt(),
                    assigned = map[FieldNames.ASSIGNED] as List<String>,
                    masterProject = map[FieldNames.MASTER_PROJECT] as Boolean,
                    bachelorProject = map[FieldNames.BACHELOR_PROJECT] as Boolean,
                    tags = map[FieldNames.TAGS] as List<String>,
                    isTaken = map[FieldNames.IS_TAKEN] as Boolean,
                    description = map[FieldNames.DESCRIPTION] as String,
                    videoURI = map[FieldNames.VIDEO_URI] as List<String>,
                    allowedSections = map[FieldNames.ALLOWED_SECTIONS] as List<String>
                )
                return when (result) {
                    is Success -> result.value
                    is Failure -> {
                        println(result.reason)
                        null
                    }
                }
                /* These two exceptions occur only with corrupted Projects so should be ignored */
            } catch (e: NullPointerException) {
                e.printStackTrace()
                return null
            } catch (e: ClassCastException) {
                e.printStackTrace()
                return null
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
        authorId: String = this.authorId,
        teacher: String = this.teacher,
        TA: String = this.TA,
        nbParticipant: Int = this.nbParticipant,
        assigned: List<String> = this.assigned,
        masterProject: Boolean = this.masterProject,
        bachelorProject: Boolean = this.bachelorProject,
        tags: List<String> = this.tags,
        isTaken: Boolean = this.isTaken,
        description: String = this.description,
        videoURI: List<String> = this.videoURI,
        allowedSections: List<String> = this.allowedSections
    ) = build(
        id, name, lab, authorId, teacher, TA, nbParticipant, assigned, masterProject, bachelorProject,
        tags, isTaken, description, videoURI, allowedSections
    )

    /**
     *  Give a Map<String,Any> the maps name of members to their values.
     */
    fun toMapString() = hashMapOf(
        FieldNames.NAME to name,
        FieldNames.NAME.toSearchName() to name.toLowerCase(Locale.ROOT).split(" "),
        FieldNames.LAB to lab,
        FieldNames.LAB.toSearchName() to lab.toLowerCase(Locale.ROOT),
        FieldNames.AUTHOR_ID to authorId,
        FieldNames.TEACHER to teacher,
        FieldNames.TEACHER.toSearchName() to teacher.toLowerCase(Locale.ROOT).split(" "),
        FieldNames.TA to TA,
        FieldNames.TA.toSearchName() to TA.toLowerCase(Locale.ROOT),
        FieldNames.NB_PARTICIPANT to nbParticipant,
        FieldNames.ASSIGNED to assigned,
        FieldNames.MASTER_PROJECT to masterProject,
        FieldNames.BACHELOR_PROJECT to bachelorProject,
        FieldNames.TAGS to tags,
        FieldNames.TAGS.toSearchName() to tags.map { it.toLowerCase(Locale.ROOT) },
        FieldNames.IS_TAKEN to isTaken,
        FieldNames.DESCRIPTION to description,
        FieldNames.VIDEO_URI to videoURI,
        FieldNames.ALLOWED_SECTIONS to allowedSections
    )





}