package com.sdp13epfl2021.projmag.video

import java.util.concurrent.TimeUnit


/**
 * This builds some subtile for the video, using text along a time stamp
 * Then you can use `build()` to return the generated subtitle file
 */
class SubtitleBuilder {
    companion object {

        const val WEBVTT = "WEBVTT"

        fun Int.webvttTime(): String {
            val sec = (TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % 60)
                .toString()
                .padStart(2, '0')
            val min = (TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % 60)
                .toString()
                .padStart(2, '0')
            val hour = TimeUnit.MILLISECONDS.toHours(this.toLong())
                .toString()
                .padStart(2, '0')
            val millis = ((this / 10) % 1000)
                .toString()
                .padStart(3, '0')
            return "$hour:$min:$sec.$millis"
        }
    }

    /**
     * The start of the current subtitle
     */
    var start: Int = 0
        set(pos) {
            field = pos
            if (end < pos) {
                end = pos
            }
        }

    /**
     * the end of the current subtitle
     */
    var end: Int = 0
        set(pos) {
            if (pos < start) {
                start = pos
            }
            field = pos
        }

    private val subtitle: MutableList<SubtitlePart> = mutableListOf()

    /**
     * Add text to the current subtitle with `start` and `end` timestamps
     *
     * @param content the text of the subtitle
     */
    fun add(content: String) {
        subtitle.add(SubtitlePart(content, start, end))
    }

    /**
     * Build the subtitle file and returns it
     *
     * @return the subtitle file
     */
    fun build(): String {
        val str = "$WEBVTT\n\n"
        return subtitle.fold(str) { s, e -> s + (e.toString()) }
    }

    fun setStartOrEnd(start: Any, defaultTimestamp: Int) {

    }

    private data class SubtitlePart(val content: String, val start: Int, val end: Int) {
        override fun toString(): String {
            return "${start.webvttTime()} --> ${end.webvttTime()}\n$content\n\n"
        }
    }
}