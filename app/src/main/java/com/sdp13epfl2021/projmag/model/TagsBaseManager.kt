package com.sdp13epfl2021.projmag.model

import java.util.regex.Matcher
import java.util.regex.Pattern

class TagsBaseManager {
    val MAX_TAG_SIZE: Int = 40

    //this should be thread safe
    companion object TagsBase {
        private val tags: MutableSet<Tag> = mutableSetOf(
            Tag("chemistry"),
            Tag("biology"), Tag("signal processing"), Tag("robotics"),
            Tag("software"), Tag("vhdl"), Tag("crypto"),
            Tag("aerodynamics"), Tag("C"), Tag("C++"),
            Tag("data Science"), Tag("scala"), Tag("java"),
            Tag("ml"), Tag("App development"), Tag("geology"),
            Tag("finance"), Tag("embedded"), Tag("energy"), Tag("photonics"),
            Tag("video processing"), Tag("electrical machines")
        )


    }


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
    fun addTag(tag: Tag): InputResult {
        val tagName = tag.name

        if (tagName.length > MAX_TAG_SIZE) {
            //sealed class?
            return InputResult.TooLong
        }


        //checking that there are other characters than normal letters :
        val p = Pattern.compile("[^a-z ]")
        val m: Matcher = p.matcher(tagName)
        if (m.find()) {
            return InputResult.ContainsSpecialChar
        }

        synchronized(tags) {
            //various other checks :
            if (tags.contains(tag)) {
                return InputResult.AlreadyExists
            } else {
                tags.add(tag.copy())
                return InputResult.OK
            }
        }

    }

    //should I be making a defensive copy here?
    fun getAllTags(): Set<Tag> {
        //defensive copy
        return tags.toSet()
    }

    fun tagsListToStringList(tags: Set<Tag>): List<String> {
        return tags.toList().map(Tag::name)
    }


    fun isListOfStringsValidTags(listTags: List<String>): Boolean {
        for (tag in listTags) {
            if (!tags.contains(Tag(tag))) {
                return false
            }
        }
        return true
    }

}



