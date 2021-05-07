package com.sdp13epfl2021.projmag.model

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class SectionBaseManagerTest {

    @Test
    fun sectionInvalidListTest(){
        val invalidList = listOf("Chemistry", "Communication Systems", "pooping" )
        Assert.assertEquals(false,SectionBaseManager.isListValid(invalidList) )
        assert(invalidList != SectionBaseManager.sectionList())

    }


    
}