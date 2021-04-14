package com.sdp13epfl2021.projmag.video

import android.content.Intent
import android.media.MediaFormat
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.FORM_TO_SUBTITLE_MESSAGE
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.video.SubtitleBuilder.Companion.webvttTime
import java.util.*

class VideoSubtitlingActivity : AppCompatActivity() {

    companion object {
        const val RESULT_KEY = "com.sdp13epfl2021.projmag.video.VideoSubtitling"
    }

    private var videoUri: Uri? = null

    private var builder = SubtitleBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_subtitling)

        videoUri = intent.getStringExtra(FORM_TO_SUBTITLE_MESSAGE)?.let { Uri.parse(it) }

        findViewById<Button>(R.id.video_subtitling_set_start_button)?.setOnClickListener {
            setStartOrEnd(SubtitleBuilder.START)
        }

        findViewById<Button>(R.id.video_subtitling_set_end_button)?.setOnClickListener {
            setStartOrEnd(SubtitleBuilder.END)
        }

        findViewById<Button>(R.id.video_subtitling_add)?.setOnClickListener { addButtonPressed() }

        findViewById<Button>(R.id.video_subtitling_submit_button)?.setOnClickListener { submit() }

        handleVideoUri()
        updateTimeTextView()
    }

    private fun handleVideoUri() {
        if (videoUri == null) {
            Toast.makeText(
                this,
                getString(R.string.video_subtitling_cant_open_video),
                Toast.LENGTH_LONG
            ).show()
        } else {
            val mediaController = MediaController(this)
            val videoView = findViewById<VideoView>(R.id.video_subtitling_videoview)

            mediaController.setAnchorView(videoView)
            mediaController.setMediaPlayer(videoView)
            videoView.setVideoURI(videoUri)
            videoView.setMediaController(mediaController)
            showInstruction()
            videoView.start()
        }
    }

    private fun showInstruction() =
        Toast.makeText(
            this,
            getString(R.string.video_subtitling_instruction),
            Toast.LENGTH_LONG
        ).show()

    private fun updateTimeTextView() {
        findViewById<TextView>(R.id.video_subtitling_start_time)?.apply {
            text = builder.start.webvttTime()
        }
        findViewById<TextView>(R.id.video_subtitling_end_time)?.apply {
            text = builder.end.webvttTime()
        }
    }

    private fun updateSubs() =
        findViewById<VideoView>(R.id.video_subtitling_videoview)?.addSubtitleSource(
            builder.build().byteInputStream(),
            MediaFormat.createSubtitleFormat("text/vtt", Locale.ENGLISH.language)
        )

    private fun addButtonPressed() =
        findViewById<EditText>(R.id.video_subtitling_subtitle_text)?.let {
            builder.add(it.text.toString())
            updateSubs()
        }

    private fun setStartOrEnd(or: Boolean) {
        findViewById<VideoView>(R.id.video_subtitling_videoview)?.let { videoView ->
            if (or == SubtitleBuilder.END) {
                videoView.pause()
            }
            builder.setStartOrEnd(or, videoView.currentPosition)
            updateTimeTextView()
        }
    }

    private fun submit() {
        val data = Intent()
        data.putExtra(RESULT_KEY, builder.build())
        setResult(RESULT_OK, data)
        finish()
    }
}
