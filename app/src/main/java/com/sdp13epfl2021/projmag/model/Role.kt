package com.sdp13epfl2021.projmag.model

enum class Role {
    STUDENT {
        override fun toString(): String {
            return "Student"
        }
    },
    TEACHER {
        override fun toString(): String {
            return "Teacher"
        }
    },
    OTHER {
        override fun toString(): String {
            return "Other"
        }
    }
}