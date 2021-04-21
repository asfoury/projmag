package com.sdp13epfl2021.projmag.video

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaFormat
import android.provider.Settings
import com.sdp13epfl2021.projmag.R
import java.util.*

object VideoUtils {
    private const val format = "text/vtt"

    /**
     * name of shared preferences for showing message related to caption only once
     */
    const val FIRST_TIME_CAPTIONS_SETTINGS_PREFERENCES = "captions_settings_preferences"

    /**
     * Subtitle format for english subtitles
     */
    val ENGLISH_WEBVTT_SUBTITLE_FORMAT = createVtt(Locale.ENGLISH.language)

    private fun createVtt(lang: String) = MediaFormat.createSubtitleFormat(format, lang)

    /**
     * Show an instruction dialog to enable captions
     *
     * @param context the activity context
     * @param pref the shared preferences (i.e. with `activity.getPreferences(...)`)
     */
    fun showInstructionDialogIfFirstTime(context: Context) {
        val pref = context.getSharedPreferences(
            FIRST_TIME_CAPTIONS_SETTINGS_PREFERENCES,
            Context.MODE_PRIVATE
        )

        fun setAsViewed() =
            pref.edit().putBoolean(FIRST_TIME_CAPTIONS_SETTINGS_PREFERENCES, true)
                .apply()


        pref.getBoolean(FIRST_TIME_CAPTIONS_SETTINGS_PREFERENCES, false).let {
            if (!it) {
                AlertDialog.Builder(context)
                    .setMessage(R.string.video_subtitling_instruction)
                    .setNegativeButton(R.string.ok) { _, _ ->
                        setAsViewed()
                    }
                    .setPositiveButton(R.string.settings) { _, _ ->
                        setAsViewed()
                        context.startActivity(Intent(Settings.ACTION_CAPTIONING_SETTINGS))
                    }.show()
            }
        }

    }

}