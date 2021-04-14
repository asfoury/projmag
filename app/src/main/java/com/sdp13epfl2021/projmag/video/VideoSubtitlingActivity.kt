package com.sdp13epfl2021.projmag.video

import android.media.MediaFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.FORM_TO_SUBTITLE_MESSAGE
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.video.SubtitleBuilder.Companion.webvttTime
import org.w3c.dom.Text
import java.util.*

class VideoSubtitlingActivity : AppCompatActivity() {


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
            finish()
        } else {
            val mediaController = MediaController(this)
            val videoView = findViewById<VideoView>(R.id.video_subtitling_videoview)

            mediaController.setAnchorView(videoView)
            mediaController.setMediaPlayer(videoView)
            videoView.setVideoURI(videoUri)
            videoView.setMediaController(mediaController)
            //val sub = "WEBVTT\n\n00:00:00.800 --> 00:00:04.115\nSubtitling Text".byteInputStream()
            videoView.start()
        }
    }

    private fun updateTimeTextView() {
        findViewById<TextView>(R.id.video_subtitling_start_time)?.apply {
            text = builder.start.webvttTime()
        }
        findViewById<TextView>(R.id.video_subtitling_end_time)?.apply {
            text = builder.end.webvttTime()
        }
    }

    private fun addButtonPressed() =
        findViewById<EditText>(R.id.video_subtitling_subtitle_text)?.let {
            builder.add(it.text.toString())
            Log.d("test", builder.build())
            findViewById<VideoView>(R.id.video_subtitling_videoview)?.let { vv ->
                Log.d("test", builder.build())
                vv.addSubtitleSource(
                    builder.build().byteInputStream(),
                    MediaFormat.createSubtitleFormat("text/vtt", Locale.ENGLISH.language)
                )
                vv.start()
            }
        }


    private fun setStartOrEnd(or: Boolean) {
        findViewById<VideoView>(R.id.video_subtitling_videoview)?.let { videoView ->
            videoView.pause()
            builder.setStartOrEnd(or, videoView.currentPosition)
            updateTimeTextView()
        }
    }
}
