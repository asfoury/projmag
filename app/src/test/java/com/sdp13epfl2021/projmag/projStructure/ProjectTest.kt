package com.sdp13epfl2021.projmag.projStructure

import com.sdp13epfl2021.projStructure.Project
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

    }

    @Test
    fun setingAndUnsettingWorks(){
        val project1 = Project("project1", "kaouCorpLabs",
                "kaou", "this is a super awesome project")

        //initially project has to be free (so taken is false)
        assertEquals(false, project1.isTaken())

        project1.setTaken();
        assertEquals(true, project1.isTaken())

        //testing that an exeption is thrown if a project is taken when it's already taken
        assertThrows(IllegalStateException::class.java) { project1.setTaken()}
    }


}