package com.sdp13epfl2021.projmag.curriculumvitae

import android.net.Uri
import java.time.LocalDate
import java.util.*


/**
 * A curriculum vitae
 */
data class CurriculumVitae(
    val summary: String,
    val education: List<PeriodDescription>,
    val jobExperience: List<PeriodDescription>,
    val awards: List<AwardDescription>,
    val languages: List<LanguageLevel>,
    val skills: List<SkillDescription>,
    val interests: List<Interests>,
    val contact: Contact,
    val references: List<Contact>,
    val picture: Uri
) {
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
         *  Description of an award or some prize
         */
        data class AwardDescription(
            val name: String,
            val description: String,
            val date: Int
        )

        /**
         * A language with the level
         */
        data class LanguageLevel(
            val language: String,
            val level: Level
        ) {
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
        }

        /**
         * Skill and the level at it
         */
        data class SkillDescription(
            val name: String,
            val level: Float
        )

        /**
         * Contact of a person or some entity
         */
        data class Contact(
            val firstName: String,
            val surName: String,
            val address: String,
            val phoneNumbers: List<String>,
            val emails: List<String>
        )

        /**
         * Interest with description
         */
        data class Interests(
            val name: String,
            val description: String
        )
    }
}
