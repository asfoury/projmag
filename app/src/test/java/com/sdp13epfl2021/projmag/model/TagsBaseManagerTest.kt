package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagsBaseManagerTest {
    @Test
    fun tagAddTest(){
        val tagBase = TagsBaseManager()
        tagBase.addTag("hellogoodsir");

    }

    @Test
    fun tagLengthTest(){
        val tagsBase = TagsBaseManager()
        assert(tagsBase.maxTagSize() == 40)
    }

    @Test
    fun tagAddTestErrors(){
        val tagBase = TagsBaseManager()
        tagBase.addTag("Machine learning")

        //tag is too long
        val error = tagBase.addTag("voluntarily going over the maximum character limit " +
                "wheep doop doop da dooble dooble do do do")
        Assert.assertEquals(TagsBaseManager.InputResult.TooLong, error)

        //tag already exists
        val error1 = tagBase.addTag("machinelearning")
        Assert.assertEquals(TagsBaseManager.InputResult.AlreadyExists, error1)

        //tag contains special characters
        val error2 = tagBase.addTag("machine1learning")
        val error3 = tagBase.addTag("machineélearning")
        Assert.assertEquals(TagsBaseManager.InputResult.ContainsSpecialChar, error2)
        Assert.assertEquals(TagsBaseManager.InputResult.ContainsSpecialChar, error3)

        //another good tag
        val success = tagBase.addTag("Software engineering")
        Assert.assertEquals(TagsBaseManager.InputResult.OK, success)


        val tags = tagBase.getAllTags()
        assert(tags.contains("machinelearning"))
        assert(tags.contains("softwareengineering"))


    }
}