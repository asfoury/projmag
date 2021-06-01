package com.sdp13epfl2021.projmag.model

import java.util.*

/**
 * This Enum is used to describe user gender
 */
enum class Gender {
    MALE {
        override fun toString(): String {
            return "Male"
        }
    },
    FEMALE {
        override fun toString(): String {
            return "Female"
        }
    },
    OTHER {
        override fun toString(): String {
            return "Other"
        }
    };

    companion object {
        fun enumOf(s: String?): Gender? {
            return when (s?.toUpperCase(Locale.getDefault())) {
                MALE.name -> MALE
                FEMALE.name -> FEMALE
                OTHER.name -> OTHER
                else -> null
            }
        }
    }
}