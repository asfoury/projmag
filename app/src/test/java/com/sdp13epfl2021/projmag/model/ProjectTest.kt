package com.sdp13epfl2021.projmag.model

import junit.framework.Assert.assertEquals
import org.junit.Test

class ProjectTest {
    @Test
    fun canCreateAproject() {
        val project = Project("projectname","teacher","teacher","ta",1, listOf(),true,false, listOf("tag1","tag2"),false,"description of project")
        assert(project != null)
        assertEquals("projectname", project.name)
    }
}