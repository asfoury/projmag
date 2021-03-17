package com.sdp13epfl2021.projmag.database

class ProjectChange(val type: Type, val project: Project) {
    enum class Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}