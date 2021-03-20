package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagsBaseTest {
    @Test
    fun tagAddTest(){
        TagsBase.addTag("hellogoodsir");
        assert(TagsBase.contains("hellogoodsir"))

    }

    @Test
    fun tagLengthTest(){
        assert(TagsBase.maxTagSize() == 40)
    }

    @Test
    fun tagAddTestErrors(){
        TagsBase.addTag("Machine learning")

        //tag is too long
        val error = TagsBase.addTag("voluntarily going over the maximum character limit " +
                "wheep doop doop da dooble dooble do do do")
        Assert.assertEquals(TagsBase.InputResult.TooLong, error)

        //tag already exists
        val error1 = TagsBase.addTag("machinelearning")
        Assert.assertEquals(TagsBase.InputResult.AlreadyExists, error1)

        //tag contains special characters
        val error2 = TagsBase.addTag("machine1learning")
        val error3 = TagsBase.addTag("machineélearning")
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error2)
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error3)

        //another good tag
        val success = TagsBase.addTag("Software engineering")
        Assert.assertEquals(TagsBase.InputResult.OK, success)


        val tags = TagsBase.getAllTags()
        assert(tags.contains("machinelearning"))
        assert(tags.contains("softwareengineering"))




    }
}