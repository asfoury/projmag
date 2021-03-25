package com.sdp13epfl2021.projmag.curriculumvitae.fragments

object CVUtils {

    /**
     * Remove duplicate in this MutableList
     */
    fun <T> MutableList<T>.mutDistinct() {
        val tmp = this.distinct()
        this.clear()
        this.addAll(tmp)
    }
}