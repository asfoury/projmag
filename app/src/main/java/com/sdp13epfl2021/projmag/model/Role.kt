package com.sdp13epfl2021.projmag.model

/**
 * This Enum is used to describe user role
 */
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