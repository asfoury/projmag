package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagTest {
    @Test
    fun tagTests(){
        val nice = "nice"
        val notnice = "notnice"


        //tag creation works
        val tag0 = Tag(nice)
        val tag1 = Tag(notnice)

        //toString method works
        Assert.assertEquals(tag0.toString(), nice)

        //duplicate works and tag equality works as intended by comparing only string
        val duplicateTag0 = tag0.copy()
        Assert.assertEquals(tag0, duplicateTag0)

        //hashcode is the same between duplicates
        Assert.assertEquals(tag0.hashCode(), duplicateTag0.hashCode())

    }
}