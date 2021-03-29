package com.sdp13epfl2021.projmag.curriculumvitae.fragments

object CVUtils {

    /**
     * Add to the MutableList if not already present
     */
    fun <T> MutableList<T>.addNotExisting(e: T) {
        if (e !in this) {
            add(e)
        }
    }
}