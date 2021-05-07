@file:Suppress("unused")

package com.sdp13epfl2021.projmag.curriculumvitae

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
/**
 * This a representation of a Curriculum Vitae
 *
 * @property summary the summary of the CV
 * @property education the list of educations the the person received
 * @property jobExperience the list of jobs the person had, its professional experience
 * @property languages the list of languages, along their respective levels
 * @property skills various other skills
 */
data class CurriculumVitae(
    val summary: String,
    val education: List<PeriodDescription>,
    val jobExperience: List<PeriodDescription>,
    val languages: List<Language>,
    val skills: List<SkillDescription>,
) : Parcelable, Serializable {


    @Parcelize
    /**
     * Description a job/school over a period of time
     *
     * @property name the name of the occupation concerned by this period
     * @property location the name of the location of this period of activity
     * @property description a description of what has been done during this period
     * @property from the beginning year
     * @property to the ending year
     */
    data class PeriodDescription(
        val name: String,
        val location: String,
        val description: String,
        val from: Int,
        val to: Int
    ) : Validateable, Parcelable, Serializable {

        private val VALID_YEARS = 1900..2100

        override fun isValid(): Boolean = name.isNotEmpty() &&
                location.isNotEmpty() &&
                description.isNotEmpty() &&
                from in VALID_YEARS &&
                to in VALID_YEARS &&
                from <= to

        override fun toString(): String =
            "$name ($from - $to)"
    }


    @Parcelize
    /**
     * A language with the level
     *
     * @property language the name of the language
     * @property level the level at this language
     */
    data class Language(
        val language: String,
        val level: Level
    ) : Validateable, Parcelable, Serializable {

        override fun isValid(): Boolean = language.isNotEmpty()

        override fun equals(other: Any?): Boolean =
            if (other is Language) {
                this.language == other.language
            } else {
                false
            }


        override fun toString(): String =
            "$language ($level)"

        override fun hashCode(): Int {
            return language.hashCode()
        }

        /**
         * Possible language levels
         */
        enum class Level(private val level: String) {
            /** Basic language level */
            Basic("Basic"),

            /** Conversational language level */
            Conversational("Conversational"),

            /** Proficient language level */
            Proficient("Proficient"),

            /** Fluent language level */
            Fluent("Fluent");

            override fun toString(): String = level
        }

    }


    @Parcelize
    /**
     * Skill and the level at it
     * @property name the name of the skill
     * @property skillLevel the level at the skill
     */
    data class SkillDescription(
        val name: String,
        val skillLevel: SkillLevel
    ) : Validateable, Parcelable, Serializable {

        /**
         * Possible language levels
         */
        enum class SkillLevel(private val level: String) {
            /** Basic skill level */
            Basic("Basic"),

            /** Normal skill level */
            Normal("Normal"),

            /** Expert skill level */
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

/**
 * An interface that tells if the object is valid or not.
 */
interface Validateable {
    /**
     * Tells if the object is valid or not.
     */
    fun isValid(): Boolean
}
