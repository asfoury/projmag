package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class GenderTest {

    @Test
    fun genderToStringIsCorrect() {
        assertEquals("Male", Gender.MALE.toString())
        assertEquals("Female", Gender.FEMALE.toString())
        assertEquals("Other", Gender.OTHER.toString())
    }
}