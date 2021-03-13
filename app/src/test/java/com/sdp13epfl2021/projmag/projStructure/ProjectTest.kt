package com.sdp13epfl2021.projmag.projStructure

import com.sdp13epfl2021.projStructure.AbstractProject
import com.sdp13epfl2021.projStructure.Project
import com.sdp13epfl2021.projStructure.TagsBase
import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalStateException

class ProjectTest {
    @Test
    fun creatingProjectWorks(){
        val project1 = Project("project1", "kaouCorpLabs",
                "kaou", "this is a super awesome project")
        assert(project1.projectOwner.equals("kaou"))
        assert(project1.projectDescription.equals("this is a super awesome project"))
        assert(project1.projectName.equals("project1"))
        assert(project1.labName.equals("kaouCorpLabs"))

    }

    @Test
    fun setingAndUnsettingWorks(){
        val project1 = Project("project1", "kaouCorpLabs",
                "kaou", "this is a super awesome project")

        //initially project has to be free (so taken is false)
        assertEquals(false, project1.isTaken())

        project1.setTaken();
        assertEquals(true, project1.isTaken())
        project1.setFree()
        assertEquals(false, project1.isTaken())

        project1.setTaken()
        //testing that an exeption is thrown if a project is taken when it's already taken
        assertEquals(AbstractProject.results.userErrorProjectTaken, project1.setTaken())

    }


}