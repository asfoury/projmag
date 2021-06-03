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

    @Test
    fun genderEnumOfWorks() {
        assertEquals(null, Gender.enumOf(null))
        assertEquals(null, Gender.enumOf(""))
        assertEquals(null, Gender.enumOf("abc"))

        assertEquals(Gender.MALE, Gender.enumOf("Male"))
        assertEquals(Gender.MALE, Gender.enumOf("MALE"))
        assertEquals(Gender.MALE, Gender.enumOf("mALe"))

        assertEquals(Gender.FEMALE, Gender.enumOf("Female"))
        assertEquals(Gender.FEMALE, Gender.enumOf("FEMALE"))
        assertEquals(Gender.FEMALE, Gender.enumOf("femALe"))

        assertEquals(Gender.OTHER, Gender.enumOf("Other"))
        assertEquals(Gender.OTHER, Gender.enumOf("OTHER"))
        assertEquals(Gender.OTHER, Gender.enumOf("oTHer"))
    }
}