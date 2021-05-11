package com.sdp13epfl2021.projmag.curriculumvitae.fragments


/**
 * Add to the MutableList if not already present
 *
 * @param e some element to add to this `MutableList`
 */
fun <T> MutableList<T>.addIfNotExisting(e: T) {
    if (e !in this) {
        add(e)
    }
}