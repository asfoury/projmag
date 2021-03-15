package com.sdp13epfl2021.projmag.data

import org.junit.Assert
import org.junit.Test

class DatasourceTest {
    @Test
    fun loadProjectsIsCorrect() {
        val projects = Datasource().loadProjects()
        assert(projects != null)
    }
}