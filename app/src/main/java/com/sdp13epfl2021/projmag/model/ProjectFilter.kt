package com.sdp13epfl2021.projmag.model

import com.sdp13epfl2021.projmag.database.ProjectId

/**
 *  An Immutable project Filter
 *  It's constructor takes filtering parameters and its `invoke` function operator
 *  takes a project and return `true`/`false`, whether it matches or not the constructor
 *  parameters
 *
 * @property bachelor if it is true will require the project degree be bachelor, otherwise it has
 *                    no effect on filtering.
 * @property master if it is true will require the project degree to be master, otherwise it has
 *                  no effect on filtering.
 * @property applied if it is true will require the project to be one the user applied to
 *                   (i.e. `isAppliedProject` is true), otherwise it has no effect on filtering.
 * @property isAppliedProject Should return true if the user applied to this project,
 *                              false otherwise.
 */
class ProjectFilter(
    val bachelor: Boolean = false,
    val master: Boolean = false,
    val applied: Boolean = false,
    val isAppliedProject: (ImmutableProject) -> Boolean = { false },
) {
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
            matches = matches && isAppliedProject(project)
        }
        return matches
    }
}
