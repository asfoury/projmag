package com.sdp13epfl2021.projmag.data

import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ImmutableProject

class Datasource {
    fun loadProjects() : List<ImmutableProject> {
        return Utils.projectsDatabase.getAllProjects()
    }
}