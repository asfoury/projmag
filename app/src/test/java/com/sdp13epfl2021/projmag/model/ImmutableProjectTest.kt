package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase.*
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class ImmutableProjectTest {
    private val id = "zoerjfoerfj"
    private val name = "epic roblox coding"
    private val labName = "roblox labs"
    private val projectManager = "kaou el roblox master"
    private val teacher = "kaou el roblox master"
    private val description = "epic roblox coding alll freaking day DAMN SON"
    private val numberStudents = 3
    private val masterProject = true
    private val bachelorSemesterProject = true
    private val masterSemesterProject = true
    private val listStudents = listOf("epic robloxxx programmer")
    private val tagManager = TagsBaseManager()
    private val tagList = tagManager.tagsListToStringList(tagManager.getAllTags())
    private val sectionManager = SectionBaseManager()
    private val sectionList = SectionBaseManager().sectionList().toList()


    val result = ImmutableProject.build(id, name, labName, projectManager, teacher, numberStudents,
        listStudents, true, true, tagList, false, description,
        listOf(), sectionList) as Success<ImmutableProject>
    val project = result.value
    @Test
    fun initializationAndSanitizationTests(){
        val tags = mutableListOf<String>()
        val sections = mutableListOf<String>()
        if(tagList.isNotEmpty()) {
            tags.add(tagList[0])
        }
        if(sectionList.isNotEmpty()){
            sections.add(sectionList[0])
        }


        val result = ImmutableProject.build(id, name, labName, projectManager, teacher, numberStudents,
            listStudents, true, true, tags, false, description,
            listOf(), sections)
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
                Assert.assertEquals(tags, project.tags)
                Assert.assertEquals(sections, project.allowedSections)
                //testing the limit of functions
                val longName = "ultra epic robloxx coder ultimate guy but with a name that's long aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                when (project.rebuild(name = longName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val longLabName = "ultra epic robloxxx lab but the name is too long"
                when (project.rebuild(lab = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when (project.rebuild(TA = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when (project.rebuild(teacher = longLabName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }


                /*val ultraLongDescription =
                    "this right here is the next generation ultra epic giga robloxx lab." +
                            " What we do is pure unalderated robloxx epicness. Using robloxx code blocks " +
                            "we are able to recreate reality as we like. The goal would be to reach the anperamthian nebula " +
                            "to destroy the center of the jedi stronghold. Yoda, the generator of the tool they call the force, " +
                            "is expected to be on their hidden stronghold. His fall should ensure our victory."+
                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                */
                val ultraLongDescription = "a".repeat(ImmutableProject.MAX_DESCRIPTION_SIZE + 1)
                when (project.rebuild(description = ultraLongDescription)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val numberStudents = 100
                when (project.rebuild(nbParticipant = numberStudents)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when(project.rebuild(assigned = listOf("aaa", "bbb", "ccc"), nbParticipant = 2)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

            }
            is Failure -> assert(false)
        }
    }


    @Test
    fun toMapStringIsCorrect() {



        val fooMap = project.toMapString()

        @Suppress("UNCHECKED_CAST")
        val projectRebuilt = fooMap.run {
            ImmutableProject(
                id = id,
                name = get("name") as String,
                lab = get("lab") as String,
                teacher = get("teacher") as String,
                TA = get("TA") as String,
                nbParticipant = get("nbParticipant") as Int,
                assigned = get("assigned") as List<String>,
                masterProject = get("masterProject") as Boolean,
                bachelorProject = get("bachelorProject") as Boolean,
                tags = get("tags") as List<String>,
                isTaken = get("isTaken") as Boolean,
                description = get("description") as String,
                allowedSections = get("allowedSections") as List<String>
            )
        }

        assertEquals(project, projectRebuilt)
    }

    @Test
    fun buildFromMapWorks() {


        val validMap: Map<String, Any?> = mapOf(
            "name" to project.name,
            "lab" to project.lab,
            "teacher" to project.teacher,
            "TA" to project.TA,
            "nbParticipant" to project.nbParticipant,
            "assigned" to project.assigned,
            "masterProject" to project.masterProject,
            "bachelorProject" to project.bachelorProject,
            "tags" to project.tags,
            "isTaken" to project.isTaken,
            "description" to project.description,
            "videoURI" to project.videoURI,
            "allowedSections" to project.allowedSections
        )


        assertNull(ImmutableProject.buildFromMap(emptyMap(), project.id))

        assertNull(ImmutableProject.buildFromMap(validMap - "name", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "lab", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "teacher", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "TA", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "nbParticipant", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "assigned", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "masterProject", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "bachelorProject", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "tags", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "isTaken", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "description", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "tags", project.id))
        assertNull(ImmutableProject.buildFromMap(validMap - "videoURI", project.id))

        assertNull(ImmutableProject.buildFromMap(validMap + ("name" to 2000), project.id))
        assertNull(ImmutableProject.buildFromMap(validMap + ("assigned" to "one"), project.id))
        assertNull(ImmutableProject.buildFromMap(validMap + ("isTaken" to "True"), project.id))

        assertEquals(project, ImmutableProject.buildFromMap(validMap, project.id))
    }

}