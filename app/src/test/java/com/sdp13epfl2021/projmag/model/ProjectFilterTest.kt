package com.sdp13epfl2021.projmag.model

import org.junit.Test
import junit.framework.TestCase.assertEquals

class ProjectFilterTest {
    private val project = (ImmutableProject.build(
        id = "id",
        name = "name",
        lab = "lab",
        teacher = "teacher",
        TA = "TA",
        nbParticipant = 1,
        assigned = listOf("student"),
        masterProject = true,
        bachelorProject = false,
        tags = listOf("tag"),
        isTaken = false,
        description = "description",
        videoURI = listOf("uri")
    ) as Success<ImmutableProject>).value

    @Test
    fun filterMasterProjectWorks() {
        val filterTrue = ProjectFilter(master = true)
        val filterFalse = ProjectFilter(master = false)

        assertEquals(true, filterTrue(project))
        assertEquals(true, filterFalse(project))
    }

    @Test
    fun filterBachelorProjectWorks() {
        val filterTrue = ProjectFilter(bachelor = true)
        val filterFalse = ProjectFilter(bachelor = false)

        assertEquals(false, filterTrue(project))
        assertEquals(true, filterFalse(project))
    }
}