package com.sdp13epfl2021.projmag.model

import java.util.*

/**
 * Class for tags, they are stored in lowercase and without spaces
 *
 * @property name : name of the tag
 */
data class Tag(var name: String) {
    private var accepted : Boolean = false
    init{

        name = name.toLowerCase(Locale.ROOT).replace("\\s+".toRegex(), "")
    }

        
      fun equals(other: Tag): Boolean {
        return other.name == this.name
      }

    /*
    When the tag is transformed into a string, it has to be only his name that is transmitted
     */
    override fun toString(): String {
        return name
    }

}


