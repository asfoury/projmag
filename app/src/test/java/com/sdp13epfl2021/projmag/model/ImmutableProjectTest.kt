package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class ImmutableProjectTest {
    @Test
    fun initializationAndSanitizationTests(){
        TagsBase.addTag(Tag("robloxprog"))
        TagsBase.addTag(Tag("machinelearning"))
        val id = "zoerjfoerfj"
        val name = "epic roblox coding"
        val labName = "roblox labs"
        val projectManager = "kaou el roblox master"
        val teacher = "kaou el roblox master"
        val description = "epic roblox coding alll freaking day DAMN SON"
        val numberStudents = 3
        val masterProject = true
        val bachelorSemesterProject = true
        val masterSemesterProject = true
        val listStudents = listOf("epic robloxxx programmer")
        val tags = listOf(Tag("robloxprog"))


        val result = ImmutableProject.build(id, name, labName, projectManager, teacher, numberStudents,
            listStudents, true, true, tags, false, description)
        when(result){
            is Success -> {

                //testing the getters
                val project = result.value
                Assert.assertEquals(name, project.name)
                Assert.assertEquals(labName, project.lab)
                Assert.assertEquals(teacher, project.teacher)
                Assert.assertEquals(description, project.description)
                Assert.assertEquals(numberStudents, project.nbParticipant)
                Assert.assertEquals(masterProject, project.masterProject)
                Assert.assertEquals(bachelorSemesterProject, project.bachelorProject)
                Assert.assertEquals(id, project.id)
                Assert.assertEquals(true, project.bachelorProject)
                Assert.assertEquals(true, project.masterProject)
                Assert.assertEquals(listStudents, project.assigned)
                //testing the limit of functions
                val longName = "ultra epic robloxx coder ultimate guy but with a name that's long"
                when (project.buildCopy(name = longName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val longLabName = "ultra epic robloxxx lab but the name is too long"
                when (project.buildCopy(lab = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when (project.buildCopy(TA = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when (project.buildCopy(teacher = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val ultraLongDescription =
                    "this right here is the next generation ultra epic giga robloxx lab." +
                            " What we do is pure unalderated robloxx epicness. Using robloxx code blocks " +
                            "we are able to recreate reality as we like. The goal would be to reach the anperamthian nebula " +
                            "to destroy the center of the jedi stronghold. Yoda, the generator of the tool they call the force, " +
                            "is expected to be on their hidden stronghold. His fall should ensure our victory."
                when (project.buildCopy(description = ultraLongDescription)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val numberStudents = 100
                when (project.buildCopy(nbParticipant = numberStudents)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val wrongTag = "hello"
                when(project.addTag(Tag("hello"))){
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val goodTag = "machinelearning"
                when(project.addTag(Tag("machinelearning"))){
                    is Success -> assert(true)
                    is Failure -> assert(false)
                }

            }
            is Failure -> assert(false)
        }
    }


    @Test
    fun toMapStringIsCorrect() {
        val default_id = ""
        val project = ImmutableProject(
            name = "some project",
            lab = "some lab",
            teacher = "some teacher",
            TA = "some TA",
            nbParticipant = 2345,
            assigned = listOf("a student", "an other student"),
            masterProject = false,
            bachelorProject = true,
            isTaken = false,
            tags = listOf("tag1", "tag2", "tag3"),
            description = "some description of the project",
            id = default_id
        )

        val fooMap = project.toMapString()

        @Suppress("UNCHECKED_CAST")
        val projectRebuilt = fooMap.run {
            ImmutableProject(
                name = get("name") as String,
                lab = get("lab") as String,
                teacher = get("teacher") as String,
                TA = get("TA") as String,
                nbParticipant = get("nbParticipant") as Int,
                assigned = get("assigned") as List<String>,
                masterProject = get("masterProject") as Boolean,
                bachelorProject = get("bachelorProject") as Boolean,
                isTaken = get("isTaken") as Boolean,
                tags = get("tags") as List<String>,
                description = get("description") as String,
                id = default_id
            )
        }

        assertEquals(project, projectRebuilt)
    }



}