package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagsBaseTest {
    @Test
    fun tagAddTest(){
        val tagBase = TagsBase()
        tagBase.addTag("hellogoodsir");

    }

    @Test
    fun tagLengthTest(){
        val tagsBase = TagsBase()
        assert(tagsBase.maxTagSize() == 40)
    }

    @Test
    fun tagAddTestErrors(){
        val tagBase = TagsBase()
        tagBase.addTag("Machine learning")

        //tag is too long
        val error = tagBase.addTag("voluntarily going over the maximum character limit " +
                "wheep doop doop da dooble dooble do do do")
        Assert.assertEquals(TagsBase.InputResult.TooLong, error)

        //tag already exists
        val error1 = tagBase.addTag("machinelearning")
        Assert.assertEquals(TagsBase.InputResult.AlreadyExists, error1)

        //tag contains special characters
        val error2 = tagBase.addTag("machine1learning")
        val error3 = tagBase.addTag("machineélearning")
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error2)
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error3)

        //another good tag
        val success = tagBase.addTag("Software engineering")
        Assert.assertEquals(TagsBase.InputResult.OK, success)


        val tags = tagBase.getAllTags()
        assert(tags.contains("machinelearning"))
        assert(tags.contains("softwareengineering"))


    }
}