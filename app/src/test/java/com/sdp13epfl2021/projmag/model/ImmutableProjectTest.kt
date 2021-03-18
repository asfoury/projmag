package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class ImmutableProjectTest {
    @Test
    fun initializationAndSanitizationTests(){
        val name = "epic roblox coding"
        val labName = "roblox labs"
        val projectManager = "kaou el roblox master"
        val description = "epic roblox coding alll freaking day DAMN SON"
        val numberStudents = 3
        val masterProject = true
        val bachelorSemesterProject = true
        val masterSemesterProject = true
        val listStudents = listOf("epic robloxxx programmer")


        val result = ImmutableProject.build(name, labName, projectManager,
            description, numberStudents,
            masterProject, bachelorSemesterProject, masterSemesterProject ,
            listOf("epic robloxxx programmer"))
        when(result){
            is Success -> {

                //testing the getters
                val project = result.value
                Assert.assertEquals(name, project.name)
                Assert.assertEquals(labName, project.labName)
                Assert.assertEquals(projectManager, project.projectManager)
                Assert.assertEquals(description, project.description)
                Assert.assertEquals(numberStudents, project.numberStudents)
                Assert.assertEquals(masterProject, project.masterProject)
                Assert.assertEquals(bachelorSemesterProject, project.bachelorSemesterProject)
                Assert.assertEquals(masterSemesterProject, project.masterSemesterProject)

                //testing the limit of functions
                val longName = "ultra epic robloxx coder ultimate guy but with a name that's long"
                when( project.buildCopy(name = longName)){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val longLabName = "ultra epic robloxxx lab but the name is too long"
                when( project.buildCopy(labName = longLabName)){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when( project.buildCopy(projectManager = longLabName)){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val ultraLongDescription = "this right here is the next generation ultra epic giga robloxx lab." +
                        " What we do is pure unalderated robloxx epicness. Using robloxx code blocks " +
                        "we are able to recreate reality as we like. The goal would be to reach the anperamthian nebula " +
                        "to destroy the center of the jedi stronghold. Yoda, the generator of the tool they call the force, " +
                        "is expected to be on their hidden stronghold. His fall should ensure our victory."
                when( project.buildCopy(description = ultraLongDescription)){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val numberStudents = 100
                when(project.buildCopy(numberStudents = numberStudents)){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

            }
            is Failure -> assert(false)
        }

    }




}