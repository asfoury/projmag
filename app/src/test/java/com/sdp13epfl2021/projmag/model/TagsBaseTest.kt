package com.sdp13epfl2021.projmag.model

import org.junit.Assert
import org.junit.Test

class TagsBaseTest {
    @Test
    fun tagAddTest(){
        TagsBase.addTag(Tag("hellogoodsir"));
        assert(TagsBase.contains(Tag("hellogoodsir")))

    }

    @Test
    fun tagLengthTest(){
        assert(TagsBase.maxTagSize() == 30)
    }

    @Test
    fun tagAddTestErrors(){
        TagsBase.addTag(Tag("Machine learning"))

        //tag is too long
        val error = TagsBase.addTag(Tag("voluntarily going over the maximum character limit " +
                "wheep doop doop da dooble dooble do do do"))
        Assert.assertEquals(TagsBase.InputResult.TooLong, error)

        //tag already exists
        val error1 = TagsBase.addTag(Tag("machinelearning"))
        Assert.assertEquals(TagsBase.InputResult.AlreadyExists, error1)

        //tag contains special characters
        val error2 = TagsBase.addTag(Tag("machine1learning"))
        val error3 = TagsBase.addTag(Tag("machineélearning"))
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error2)
        Assert.assertEquals(TagsBase.InputResult.ContainsSpecialChar, error3)

        //another good tag
        val success = TagsBase.addTag(Tag("Software engineering"))
        Assert.assertEquals(TagsBase.InputResult.OK, success)


        val tags = TagsBase.getAllTags()
        tags.any{it == Tag("machinelearning")}
        assert(tags.any{it == Tag("machinelearning")})
        assert(tags.any{it == Tag("softwareengineering")})




    }
}