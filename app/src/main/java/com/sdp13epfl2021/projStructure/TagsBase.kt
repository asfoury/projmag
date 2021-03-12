package com.sdp13epfl2021.projStructure

import java.util.regex.Matcher
import java.util.regex.Pattern

data class TagsBase(
        val tags: MutableSet<String> = mutableSetOf<String>(),
        private final val MAX_TAG_SIZE : Int = 40

){
    sealed class tagAddResult {
        object TagTooLong : tagAddResult()
        object TagContainsSpecialCharacters : tagAddResult()
        object
    }

    fun maxTagSize(): Int {
        return MAX_TAG_SIZE
    }
    fun addTag(tag : String) : tagAddResult{
        if(tag.length > MAX_TAG_SIZE ){
            //what to do when I have to throw an exeption since exeption throwing is bad in kotlin
            //sealed class?
            throw IllegalArgumentException("the tag is more than $MAX_TAG_SIZE chars!");
        }
        val cleanTag = tag.toLowerCase()
        val p = Pattern.compile("[^0-9A-Za-z ]")
        val m : Matcher = p.matcher(cleanTag)
        if(m.find()){
            throw IllegalArgumentException("the tag contains special characters which is forbidden!")
        }

        if(tags.contains(cleanTag)){
            throw IllegalArgumentException("the tag was already contained in the tag list")
        }
        else if(cleanTag.equals(cleanTag.replace(" ", ""))){
            throw java.lang.IllegalArgumentException("The tag is the same as another but with or without whitespaces")
        }
        else{
            tags.add(cleanTag)
        }

    }

}
