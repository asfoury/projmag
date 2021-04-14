package com.sdp13epfl2021.projmag.model

class ProfileTeacher( lastName : String,  firstName : String,  age : Int,  gender : Gender, phoneNumber : String)
    : ProfileClass(lastName,firstName,age,gender,phoneNumber) {
    companion object{
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
         * @param phoneNumber : phone number on the profile
         * @return Success(profile) or failure(String explanation)
         */
        fun build(lastName : String, firstName : String,
                  age : Int, gender : Gender, phoneNumber :String) : Result<ProfileTeacher> {
            return when{
                lastName.length > MAX_LAST_NAME_SIZE ->  Failure("last name is more than $MAX_LAST_NAME_SIZE characters")
                firstName.length > MAX_FIRST_NAME_SIZE -> Failure("first name is more than $MAX_FIRST_NAME_SIZE characters")
                age > MAX_AGE -> Failure("age is more than $MAX_AGE")
                age <  MIN_AGE -> Failure("age is less than $MIN_AGE")
                else -> Success(ProfileTeacher(lastName,  firstName,
                    age,  gender,  phoneNumber))

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
     * @param phoneNumber : phone number on the profile
     * @return Success(profile) or failure(String explanation)
     */
    fun buildCopy(lastName: String = this.lastName, firstName : String = this.firstName,
                  age : Int = this.age, gender : Gender = this.gender,
                  phoneNumber :String = this.phoneNumber) : Result<ProfileTeacher>{
        return ProfileTeacher.build(lastName,  firstName, age,  gender, phoneNumber)

    }
}