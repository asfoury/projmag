package com.sdp13epfl2021.projStructure


public abstract class AbstractProject(
        val projectName: String, val labName: String,
        val projectOwner: String,
        val projectDescription: String,
        private val assignedStudents: String) {

    //a companion object is the way to implement static variables in kotlin
    companion object{
        private final const val TAKEN: Boolean = true
        private final const val FREE: Boolean = false

    }
    sealed class results{
        object userCommandSuccess : results()
        object userErrorProjectTaken : results()
    }
     private var isTaken = FREE;


    fun setTaken() : results{
        if(isTaken == TAKEN){
            return results.userErrorProjectTaken
        }
        isTaken = TAKEN;
        return results.userCommandSuccess
    }

    fun setFree(){
        isTaken = FREE;
    }

    fun isTaken() : Boolean {
        return isTaken;
    }







}