package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

sealed class Result<T>
data class Success<T>(val value: T) : Result<T>()
data class Failure<T>(val reason: String) : Result<T>()

class ImmutableProject(val id : String,
                       val name : String,
                       val lab : String,
                       val teacher : String,
                       val TA: String,
                       val nbParticipant : Int,
                       val assigned : List<String>,
                       val masterProject : Boolean,
                       val bachelorProject : Boolean,
                       val tags : List<Tag>,
                       val isTaken: Boolean,
                       val description: String
                        ) {
    companion object {
        const val MAX_NAME_SIZE = 40
        const val MAX_DESCRIPTION_SIZE = 300
        const val MAX_STUDENT_NUMBER = 10
        fun build(
            id: String,
            name: String,
            lab: String,
            teacher: String,
            TA: String,
            nbParticipant : Int,
            assigned : List<String>,
            masterProject : Boolean,
            bachelorProject : Boolean,
            tags : List<Tag>,
            isTaken: Boolean,
            description: String
        ): Result<ImmutableProject> {
            return when {
                name.length > MAX_NAME_SIZE -> Failure("name is more than $MAX_NAME_SIZE characters")
                lab.length > MAX_NAME_SIZE -> Failure("lab name is more than $MAX_NAME_SIZE characters")
                TA.length > MAX_NAME_SIZE -> Failure("project manager name is more than $MAX_NAME_SIZE characters")
                teacher.length > MAX_NAME_SIZE -> Failure("teacher name is more than $MAX_NAME_SIZE characters")
                description.length > MAX_DESCRIPTION_SIZE -> Failure("description  is more than $MAX_DESCRIPTION_SIZE characters")
                nbParticipant > MAX_STUDENT_NUMBER -> Failure("max student number for a project is set to $MAX_STUDENT_NUMBER")
                assigned.size > nbParticipant -> Failure(
                    "there are ${assigned.size} " +
                            "students currently assigned but only $nbParticipant allowed to work for the project"
                );
                checkAllTags(tags) -> Failure("tags used were not from the allowed tag list")
                else -> Success( ImmutableProject(
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

        //added this function so that when I add a new tag I dont have to do the full checkup of
        //the whole list of tags
        fun fastBuildForAddTag(id : String,
                               name : String,
                               lab : String,
                               teacher : String,
                               TA: String,
                               nbParticipant : Int,
                               assigned : List<String>,
                               masterProject : Boolean,
                               bachelorProject : Boolean,
                               tags : List<Tag>,
                               isTaken: Boolean,
                               description: String) : Result<ImmutableProject>{
            return Success( ImmutableProject(
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
            ))
        }

        fun checkAllTags(tags : List<Tag>): Boolean {
            for(tag in tags){
                if(!TagsBase.contains(tag)){
                    return true
                }
            }
            return false
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
    fun buildCopy(id : String = this.id,
                  name : String = this.name,
                  lab : String = this.lab,
                  teacher : String = this.teacher,
                  TA : String = this.TA,
                  nbParticipant : Int = this.nbParticipant,
                  assigned : List<String> = this.assigned,
                  masterProject: Boolean = this.masterProject,
                  bachelorProject: Boolean = this.bachelorProject,
                  tags: List<Tag> = this.tags,
                  isTaken: Boolean = this.isTaken,
                  description: String = this.description): Result<ImmutableProject> {

        return build( id, name, lab, teacher, TA, nbParticipant, assigned, masterProject, bachelorProject,
            this.tags, isTaken, description,)

    }

    fun addTag(tag : Tag): Result<ImmutableProject> {
        if(TagsBase.contains(tag)){
            return fastBuildForAddTag(this.id, this.name, this.lab, this.teacher, this.TA, nbParticipant, assigned, masterProject, bachelorProject,
                this.tags.plus(tag), isTaken, description)
        }
        else{
            return Failure("the tag wasn't contained in the database")
        }


    }

}