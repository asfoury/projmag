package com.sdp13epfl2021.projmag.model

import java.util.*

/**
 * Class for tags, they are stored in lowercase and without spaces
 *
 * @property name : name of the tag
 */
data class Tag(var name : String) {
    private var accepted : Boolean = false
    init{
        name = name.toLowerCase(Locale.ROOT).replace(" ","")
    }


      fun equals(other: Tag): Boolean {
        return other.name == this.name
      }

}


