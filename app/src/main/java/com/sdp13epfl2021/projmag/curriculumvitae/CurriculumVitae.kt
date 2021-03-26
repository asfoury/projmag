@file:Suppress("unused")

package com.sdp13epfl2021.projmag.curriculumvitae

import android.net.Uri


/**
 * A curriculum vitae
 */
data class CurriculumVitae(
    val summary: String,
    val education: List<PeriodDescription>,
    val jobExperience: List<PeriodDescription>,
    val languages: List<LanguageLevel>,
    val skills: List<SkillDescription>,
) {

    var uri: Uri? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    companion object {
        interface Validate {
            fun isValid(): Boolean
        }

        private val VALID_YEARS = 1900..2100

        /**
         * Description a job/school over a period of time
         */
        data class PeriodDescription(
            val name: String,
            val location: String,
            val description: String,
            val from: Int,
            val to: Int
        ) : Validate {
            override fun isValid(): Boolean = name.isNotEmpty() &&
                    location.isNotEmpty() &&
                    description.isNotEmpty() &&
                    from in VALID_YEARS &&
                    to in VALID_YEARS &&
                    from <= to

            override fun toString(): String =
                "$name ($from - $to)"
        }

        /**
         * A language with the level
         */
        data class LanguageLevel(
            val language: String,
            val level: Level
        ) : Validate {
            companion object {
                /**
                 * Possible language levels
                 */
                enum class Level(private val level: String) {
                    Basic("Basic"),
                    Conversational("Conversational"),
                    Proficient("Proficient"),
                    Fluent("Fluent");

                    override fun toString(): String = level
                }
            }

            override fun isValid(): Boolean = language.isNotEmpty()

            override fun equals(other: Any?): Boolean =
                if (other is LanguageLevel) {
                    this.language == other.language
                } else {
                    false
                }


            override fun toString(): String =
                "$language ($level)"

            override fun hashCode(): Int {
                return language.hashCode()
            }

        }

        /**
         * Skill and the level at it
         */
        data class SkillDescription(
            val name: String,
            val skillLevel: SkillLevel
        ) : Validate {
            /**
             * Possible language levels
             */
            enum class SkillLevel(private val level: String) {
                Basic("Basic"),
                Normal("Normal"),
                Expert("Expert");

                override fun toString(): String = level
            }

            override fun isValid(): Boolean =
                name.isNotEmpty()

            override fun toString(): String =
                "$name ($skillLevel)"

            override fun equals(other: Any?): Boolean =
                if (other is SkillDescription) {
                    this.name == other.name
                } else {
                    false
                }

            override fun hashCode(): Int {
                return name.hashCode()
            }


        }
    }

}
