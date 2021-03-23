package com.sdp13epfl2021.projmag.model


import javax.annotation.concurrent.Immutable


class ImmutableProfile private constructor(val lastName : String, val firstName : String,
                        val age : Int, val gender : genderEnum, val sciper : Int, val phoneNumber : Int) {

    companion object{
        private const val MAX_LAST_NAME_SIZE = 40
        private const val MAX_FIRST_NAME_SIZE = 40
        private const val MAX_AGE = 120
        private const val MIN_AGE = 15
        fun build( lastName : String,  firstName : String,
                   age : Int,  gender : genderEnum,  sciper : Int,  phoneNumber :Int) : Result<ImmutableProfile> {
            return when{
                lastName.length > MAX_LAST_NAME_SIZE ->  Failure("last name is more than $MAX_LAST_NAME_SIZE characters")
                firstName.length > MAX_FIRST_NAME_SIZE -> Failure("first name is more than $MAX_FIRST_NAME_SIZE characters")
                age > MAX_AGE -> Failure("age is more than $MAX_AGE")
                age <  MIN_AGE -> Failure("age is less than $MIN_AGE")
                sciper < 0 -> Failure("sciper can't be a negative value")
                else -> Success(ImmutableProfile(lastName,  firstName,
                    age,  gender,  sciper,  phoneNumber))

            }
        }

    }

    fun buildCopy(lastName: String = this.lastName, firstName : String = this.firstName,
                  age : Int = this.age, gender : genderEnum = this.gender, sciper : Int = this.sciper,
                  phoneNumber :Int = this.phoneNumber) : Result<ImmutableProfile>{
        return ImmutableProfile.build(lastName,  firstName,
            age,  gender,  sciper,  phoneNumber)

    }


}