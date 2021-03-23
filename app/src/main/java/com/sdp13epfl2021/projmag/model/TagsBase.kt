package com.sdp13epfl2021.projmag.model

import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

 object TagsBase{

     private val tags: MutableSet<Tag> = mutableSetOf()
     private  val MAX_TAG_SIZE : Int = 30
    /**
     * TagTooLong : the tag is longer than MAX_TAG_SIZE which is output by the function maxTagSize
     * TagContainsSpecialChar : the tag contains numbers, and special characters like $ £ etc
     * TagAlreadyExists : a version of this tag already exists without whitespaces or caps
     *
     */
    sealed class InputResult {
        object TooLong : InputResult()
        object ContainsSpecialChar : InputResult()
        object AlreadyExists : InputResult()
        object OK : InputResult()
    }

    /**
     * Function that gives the max size of a tag in characters
     *
     * @return max size of the tag
     */
    fun maxTagSize(): Int {
        return MAX_TAG_SIZE
    }

    /**
     * Function that checks that the tag is valid and can be added in the database of tags
     *
     * @param tag
     * @return whether or not an error has happened and what type it is using the sealed class tagAddResult
     */
    fun addTag(tag : Tag) : InputResult{
        //cleaning up the data :
        if(tag.name.length > MAX_TAG_SIZE ){
            //sealed class?
            return InputResult.TooLong
        }
        val cleanTagName = tag.name.toLowerCase(Locale.getDefault()).replace(" ","")
        val cleanTag = Tag(cleanTagName)
        //checking that there are other characters than normal letters :
        val p = Pattern.compile("[^a-z ]")
        val m : Matcher = p.matcher(cleanTagName)
        if(m.find()){
            return InputResult.ContainsSpecialChar
        }

        //various other checks :
        return if(tags.contains(cleanTag)){
            InputResult.AlreadyExists
        } else{
            tags.add(cleanTag)
            InputResult.OK
        }

    }

    //should I be making a defensive copy here?
    fun getAllTags() : Set<Tag>{
        return tags.toSet()
    }

     fun contains(tag : Tag) : Boolean{
         return tags.contains(tag)
     }

}