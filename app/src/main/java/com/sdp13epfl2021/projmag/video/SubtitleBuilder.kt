package com.sdp13epfl2021.projmag.video

import java.util.concurrent.TimeUnit

class SubtitleBuilder {
    companion object {
        const val START = true
        const val END = !START

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

    var start: Int = 0
        private set
    var end: Int = 0
        private set

    private val subtitle: MutableList<SubtitlePart> = mutableListOf()

    fun setStartOrEnd(or: Boolean, pos: Int) {
        if (or == START) {
            start = pos
            end = if (end < pos) pos else end
        } else {
            start = if (pos < start) pos else start
            end = pos
        }
    }

    fun add(content: String) {
        subtitle.add(SubtitlePart(content, start, end))
    }

    fun build(): String {
        val str = "$WEBVTT\n\n"
        return subtitle.fold(str) { s, e -> s + (e.toString()) }
    }

    private data class SubtitlePart(
        val content: String,
        val start: Int,
        val end: Int
    ) {
        override fun toString(): String {
            return "${start.webvttTime()} --> ${end.webvttTime()}\n$content\n\n"
        }
    }
}