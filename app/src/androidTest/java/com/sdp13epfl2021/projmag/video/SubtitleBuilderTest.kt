package com.sdp13epfl2021.projmag.video


import com.sdp13epfl2021.projmag.video.SubtitleBuilder.Companion.webvttTime
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SubtitleBuilderTest {
    private val timeStampStart = 0
    private val timeStampStartText = "00:00:00.000"
    private val timeStampEnd = 111111
    private val timeStampEndText = "00:01:51.111"
    private val subtitleText = "TextSubtitle"
    private val formattedWebvttSubtitle =
        "WEBVTT\n\n${timeStampStartText} --> ${timeStampEndText}\n$subtitleText\n\n"

    @Test
    fun intToWebvttTimesampIsCorrect() {
        assertEquals(timeStampStartText, timeStampStart.webvttTime())
        assertEquals(timeStampEndText, timeStampEnd.webvttTime())
    }

    @Test
    fun buildingWorks() {
        val builder = SubtitleBuilder()
        builder.start = timeStampStart
        builder.end = timeStampEnd
        builder.add(subtitleText)
        assertEquals(formattedWebvttSubtitle, builder.build())
    }
}