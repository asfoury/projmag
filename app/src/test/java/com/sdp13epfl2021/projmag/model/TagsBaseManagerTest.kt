package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagsBaseManagerTest {
    @Test
    fun tagAddTest(){
        val tagBase = TagsBaseManager()
        tagBase.addTag(Tag("hellogoodsir"));

    }

    @Test
    fun tagLengthTest(){
        val tagsBase = TagsBaseManager()
        assert(tagsBase.maxTagSize() == 40)
    }

    @Test
    fun tagAddTestErrors(){
        val tagBase = TagsBaseManager()
        tagBase.addTag(Tag("Machine learning"))

        //tag is too long
        val error = tagBase.addTag(Tag("voluntarily going over the maximum character limit " +
                "wheep doop doop da dooble dooble do do do"))
        Assert.assertEquals(TagsBaseManager.InputResult.TooLong, error)

        //tag already exists
        val error1 = tagBase.addTag(Tag("machinelearning"))
        println(tagBase.getAllTags())
        Assert.assertEquals(TagsBaseManager.InputResult.AlreadyExists, error1)

        //tag contains special characters
        val error2 = tagBase.addTag(Tag("machine1learning"))
        val error3 = tagBase.addTag(Tag("machineélearning"))
        Assert.assertEquals(TagsBaseManager.InputResult.ContainsSpecialChar, error2)
        Assert.assertEquals(TagsBaseManager.InputResult.ContainsSpecialChar, error3)

        //another good tag
        val success = tagBase.addTag(Tag("Software engineering"))
        Assert.assertEquals(TagsBaseManager.InputResult.OK, success)


        val tags = tagBase.getAllTags()
        assert(tags.contains(Tag("machinelearning")))
        assert(tags.contains(Tag("softwareengineering")))


    }
}