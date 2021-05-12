package com.sdp13epfl2021.projmag.model

/**
 * This Enum is used to describe user gender
 */
enum class Gender {
    MALE{
        override fun toString(): String {
            return "Male"
        }
        },FEMALE{
        override fun toString(): String {
            return "Female"
        }
                },OTHER{
        override fun toString(): String {
            return "Other"
        }
                }
}