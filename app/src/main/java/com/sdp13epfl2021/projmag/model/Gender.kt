package com.sdp13epfl2021.projmag.model

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