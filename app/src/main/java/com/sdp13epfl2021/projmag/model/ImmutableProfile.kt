package com.sdp13epfl2021.projmag.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * This Class is used to define a Profile
 */

@Parcelize

data class ImmutableProfile private constructor(
    val lastName: String, val firstName: String,
    val age: Int, val gender: Gender, val sciper: Int?, val phoneNumber: String, val role: Role
) :
    Parcelable, Serializable {

    companion object {
        private const val MAX_LAST_NAME_SIZE = 40
        private const val MAX_FIRST_NAME_SIZE = 40
        private const val MAX_AGE = 120
        private const val MIN_AGE = 15


        /**
         * Function that allows creating a profile with tests
         *TOOODOOOO : implement contacting firebase to get the user email and names by default prefilled?
         *
         * @param lastName : Last name of the profile
         * @param firstName : first name on the profile
         * @param age : age on the profile
         * @param gender : gender on the profile
         * @param sciper : sciper on the profile
         * @param phoneNumber : phone number on the profile
         * @param role : role on the profile
         * @return Success(profile) or failure(String explanation)
         */
        fun build(
            lastName: String, firstName: String,
            age: Int, gender: Gender, sciper: Int?, phoneNumber: String, role: Role
        ): Result<ImmutableProfile> {
            return when {
                lastName.length > MAX_LAST_NAME_SIZE -> Failure("last name is more than $MAX_LAST_NAME_SIZE characters")
                firstName.length > MAX_FIRST_NAME_SIZE -> Failure("first name is more than $MAX_FIRST_NAME_SIZE characters")
                age > MAX_AGE -> Failure("age is more than $MAX_AGE")
                age < MIN_AGE -> Failure("age is less than $MIN_AGE")
                (sciper != null && sciper < 0) -> Failure("sciper can't be a negative value")
                else -> Success(
                    ImmutableProfile(
                        lastName, firstName,
                        age, gender, sciper, phoneNumber, role
                    )
                )

            }
        }

    }

    /**
     * Function that allows creating a copy from another Immutable profile and modifying the fields
     * we want. Re runs all the tests.
     *TOOODOOOO : implement contacting firebase to get the user email and names by default prefilled?
     *
     * @param lastName : Last name of the profile
     * @param firstName : first name on the profile
     * @param age : age on the profile
     * @param gender : gender on the profile
     * @param sciper : sciper on the profile
     * @param phoneNumber : phone number on the profile
     * @param role : role on the profile
     * @return Success(profile) or failure(String explanation)
     */
    fun buildCopy(
        lastName: String = this.lastName, firstName: String = this.firstName,
        age: Int = this.age, gender: Gender = this.gender, sciper: Int? = this.sciper,
        phoneNumber: String = this.phoneNumber, role: Role = this.role
    ): Result<ImmutableProfile> {
        return ImmutableProfile.build(
            lastName, firstName,
            age, gender, sciper, phoneNumber, role
        )

    }


}
