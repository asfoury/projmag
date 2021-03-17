package com.sdp13epfl2021.projmag.database

import com.sdp13epfl2021.projmag.model.ImmutableProject

class ProjectChange(val type: Type, val project: ImmutableProject?) {
    enum class Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}