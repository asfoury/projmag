package com.sdp13epfl2021.projmag.projStructure
import com.sdp13epfl2021.projStructure.AbstractProject
import com.sdp13epfl2021.projStructure.TagsBase
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


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
        Assert.assertEquals(TagsBase.tagAddResult.TagTooLong, error)

        //tag already exists
        val error1 = tagBase.addTag("machinelearning")
        Assert.assertEquals(TagsBase.tagAddResult.TagAlreadyExists, error1)

        //tag contains special characters
        val error2 = tagBase.addTag("machine1learning")
        val error3 = tagBase.addTag("machineélearning")
        Assert.assertEquals(TagsBase.tagAddResult.TagContainsSpecialChar, error2)
        Assert.assertEquals(TagsBase.tagAddResult.TagContainsSpecialChar, error3)

        //another good tag
        val success = tagBase.addTag("Software engineering")
        Assert.assertEquals(TagsBase.tagAddResult.GoodTag, success)


        val tags = tagBase.getAllTags()
        assert(tags.contains("machinelearning"))
        assert(tags.contains("softwareengineering"))





    }


}