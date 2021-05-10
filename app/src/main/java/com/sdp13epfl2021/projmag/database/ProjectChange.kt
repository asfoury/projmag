package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.ImmutableProject

/**
 * Represent a change of project used in ProjectDatabase.
 * This can be a new project, a modification or deletion of a project.
 *
 * @param type Type of the change.
 * @param project the project that was changed.
 */
data class ProjectChange(val type: Type, val project: ImmutableProject) {
    enum class Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}