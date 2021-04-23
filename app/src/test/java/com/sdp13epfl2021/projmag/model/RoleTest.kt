package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class RoleTest {

    @Test
    fun roleToStringIsCorrect() {
        assertEquals("Student", Role.STUDENT.toString())
        assertEquals("Teacher", Role.TEACHER.toString())
        assertEquals("Other", Role.OTHER.toString())
    }
}