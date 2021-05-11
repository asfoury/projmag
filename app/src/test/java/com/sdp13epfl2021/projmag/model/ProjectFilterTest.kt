package com.sdp13epfl2021.projmag.model

import org.junit.Test
import junit.framework.TestCase.assertEquals

class ProjectFilterTest {
    private val id = "id"

    private val project = (ImmutableProject.build(
        id = id,
        authorId = "authorId",
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

    @Test
    fun appliedProjectWorks() {
        var filterApplied = ProjectFilter(applied = true)

        filterApplied.setApplicationCheck { p -> p.id == id }
        assertEquals(true, filterApplied(project))
        filterApplied.setApplicationCheck { p -> p.id != id }
        assertEquals(false, filterApplied(project))

        filterApplied = ProjectFilter(applied = false)

        filterApplied.setApplicationCheck { p -> p.id == id }
        assertEquals(true, filterApplied(project))
        filterApplied.setApplicationCheck { p -> p.id != id }
        assertEquals(true, filterApplied(project))
    }

    @Test
    fun buildFromMapWorks() {
        val map = mapOf("bachelor" to false, "master" to true, "applied" to false)
        val pf = ProjectFilter(map)
        assertEquals(false, pf.bachelor)
        assertEquals(true, pf.master)
        assertEquals(false, pf.applied)
    }

    @Test
    fun favoriteWorks(){
        var filterFavourite = ProjectFilter(favorites = true)

        filterFavourite.setFavouriteCheck { p -> p.id == id }
        assertEquals(true, filterFavourite(project))
        

    }
}