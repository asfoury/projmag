package com.sdp13epfl2021.projStructure

import java.lang.IllegalStateException

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

     private var isTaken = FREE;

    fun showProject(): String {
        return "owner : $projectOwner \nlab : $labName \ndescription : $projectDescription \n"
    }

    fun setTaken(){
        if(isTaken == TAKEN){
            throw IllegalStateException("project was already taken!")
        }
        isTaken = TAKEN;
    }

    fun setFree(){
        isTaken = FREE;
    }

    fun isTaken() : Boolean {
        return isTaken;
    }







}