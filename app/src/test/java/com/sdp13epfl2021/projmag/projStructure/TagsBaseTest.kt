package com.sdp13epfl2021.projmag.projStructure
import com.sdp13epfl2021.projStructure.TagsBase
import org.junit.Test
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


class TagsBaseTest {
    @Test(expected = IllegalArgumentException::class)
    fun tagAddTest(){
        val tagBase = TagsBase()
        tagBase.addTag("hellogoodsir");
    }

    @Test(expected = IllegalArgumentException::class)
    fun tagAddTestTooLongString(){
        val tagBase = TagsBase()
        tagBase.addTag("Machine learning")
        try{
            tagBase.addTag("I am gonna go over the limit of ${tagBase.maxTagSize()} " +
                    "  characters booop boop beedoop boop beedop")
        }
        catch(e : IllegalArgumentException) {
            println(e.toString())

        }


    }


}