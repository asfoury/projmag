package com.sdp13epfl2021.projmag.video

import android.media.MediaFormat
import java.util.*

object VideoUtils {
    private const val format = "text/vtt"
    private fun createVtt(lang: String) = MediaFormat.createSubtitleFormat(format, lang)

    val ENGLISH_LANG = Locale.ENGLISH.language

    val ENGLISH_WEBVTT_SUBTITLE_FORMAT = createVtt(ENGLISH_LANG)

}