package com.sdp13epfl2021.projmag.video

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaFormat
import android.provider.Settings
import android.widget.Toast
import com.sdp13epfl2021.projmag.R
import java.util.*

/**
 * Some Utils functions and values used to facilitate video subtitling.
 */
object VideoUtils {
    private const val format = "text/vtt"

    /**
     * Subtitle format for english subtitles
     */
    val ENGLISH_WEBVTT_SUBTITLE_FORMAT = createVtt(Locale.ENGLISH.language)

    private fun createVtt(lang: String) = MediaFormat.createSubtitleFormat(format, lang)

    /**
     * Show an instruction dialog to enable captions, once
     *
     * @param context the activity context
     */
    fun showInstructionDialog(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(R.string.video_subtitling_instruction)
            .setNegativeButton(R.string.ok) { _, _ ->
                Toast.makeText(
                    context,
                    context.getString(R.string.video_subtitling_instruction_ok_answer),
                    Toast.LENGTH_LONG
                ).show()
            }
            .setPositiveButton(R.string.settings) { _, _ ->
                context.startActivity(Intent(Settings.ACTION_CAPTIONING_SETTINGS))
            }.show()
    }

}