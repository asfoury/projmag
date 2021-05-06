package com.sdp13epfl2021.projmag.model

import com.sdp13epfl2021.projmag.database.ProjectId

data class ProjectFilter(
    val bachelor: Boolean = false,
    val master: Boolean = false,
    val applied: Boolean = false,
    val appliedProjects: List<ProjectId> = ArrayList(),
) {

    companion object {
        val default = ProjectFilter()
    }

    /**
     * Tells if the given project match the constraints
     *
     * @param project the project to check constraint
     *
     * @return `true` if it matches, `false` otherwise
     */
    operator fun invoke(project: ImmutableProject): Boolean {
        var matches = true
        if (bachelor) {
            matches = matches && project.bachelorProject
        }
        if (master) {
            matches = matches && project.masterProject
        }
        if (applied) {
            matches = matches && appliedProjects.contains(project.id)
        }
        return matches
    }
}
