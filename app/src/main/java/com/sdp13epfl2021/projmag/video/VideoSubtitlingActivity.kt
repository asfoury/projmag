package com.sdp13epfl2021.projmag.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.FORM_TO_SUBTITLE_MESSAGE
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.video.SubtitleBuilder.Companion.webvttTime


const val VIDEO_SUBTITLING_ACTIVITY_RESULT_KEY = "com.sdp13epfl2021.projmag.video.VideoSubtitling"

/**
 * The activity where the user can add subtitle with timestamps to a video
 */
class VideoSubtitlingActivity : AppCompatActivity() {

    private var videoUri: Uri? = null

    private var builder = SubtitleBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_subtitling)

        videoUri = intent.getStringExtra(FORM_TO_SUBTITLE_MESSAGE)?.let { Uri.parse(it) }

        findViewById<Button>(R.id.video_subtitling_set_start_button).setOnClickListener {
            setStart()
        }

        findViewById<Button>(R.id.video_subtitling_set_end_button).setOnClickListener {
            setEnd()
        }

        findViewById<Button>(R.id.video_subtitling_add).setOnClickListener { addButtonPressed() }

        findViewById<Button>(R.id.video_subtitling_submit_button).setOnClickListener { submit() }

        handleVideoUri()
        updateTimeTextView()
    }

    private fun handleVideoUri() {
        val mediaController = MediaController(this)
        val videoView = findViewById<VideoView>(R.id.video_subtitling_videoview)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        showToast(getString(R.string.video_subtitling_instruction))
        videoView.setVideoURI(videoUri)
        videoView.start()

        if (videoUri == null) {
            showToast(getString(R.string.video_subtitling_cant_open_video))
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_LONG
        ).show()

    private fun updateTimeTextView() {
        findViewById<TextView>(R.id.video_subtitling_start_time).apply {
            text = builder.start.webvttTime()
        }
        findViewById<TextView>(R.id.video_subtitling_end_time).apply {
            text = builder.end.webvttTime()
        }
    }

    private fun updateSubs() =
        findViewById<VideoView>(R.id.video_subtitling_videoview).addSubtitleSource(
            builder.build().byteInputStream(),
            VideoUtils.ENGLISH_WEBVTT_SUBTITLE_FORMAT
        )

    private fun addButtonPressed() =
        findViewById<EditText>(R.id.video_subtitling_subtitle_text).let {
            builder.add(it.text.toString())
            updateSubs()
        }

    private fun setStart() {
        findViewById<VideoView>(R.id.video_subtitling_videoview).let { videoView ->
            builder.start = videoView.currentPosition
            updateTimeTextView()
        }
    }

    private fun setEnd() {
        findViewById<VideoView>(R.id.video_subtitling_videoview).let { videoView ->
            videoView.pause()
            builder.end = videoView.currentPosition
            updateTimeTextView()
        }
    }

    private fun submit() {
        val data = Intent()
        data.putExtra(VIDEO_SUBTITLING_ACTIVITY_RESULT_KEY, builder.build())
        setResult(RESULT_OK, data)
        finish()
    }
}
