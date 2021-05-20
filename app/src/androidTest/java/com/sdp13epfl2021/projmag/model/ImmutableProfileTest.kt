package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ImmutableProfileTest {
    @Test
    fun profileTest() {
        val lastName: String = "tamine"
        val firstName = "kaourintin "
        val age = 21
        val gender = Gender.MALE
        val sciper = 287746
        val phoneNumber = "0763030355"
        val role = Role.STUDENT

        val result =
            ImmutableProfile.build(lastName, firstName, age, gender, sciper, phoneNumber, role)

        when (result) {
            is Success -> {
                val profile = result.value
                assertEquals(lastName, profile.lastName)
                assertEquals(firstName, profile.firstName)
                assertEquals(age, profile.age)
                assertEquals(gender, profile.gender)
                assertEquals(sciper, profile.sciper)
                assertEquals(phoneNumber, profile.phoneNumber)

                val longFirstName = "this name is way too long for the project"
                when (profile.buildCopy(firstName = longFirstName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val longLastName = "this name is way too long for the project"
                when (profile.buildCopy(lastName = longLastName)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                val bigAge = 180
                val smallAge = -10
                when (profile.buildCopy(age = bigAge)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }

                when (profile.buildCopy(age = smallAge)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }


                val wrongSciper = -30
                when (profile.buildCopy(sciper = wrongSciper)) {
                    is Success -> assert(false)
                    is Failure -> assert(true)
                }


            }
            is Failure -> {
                assert(false)
            }
        }
    }
}