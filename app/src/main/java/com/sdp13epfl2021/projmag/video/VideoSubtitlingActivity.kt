package com.sdp13epfl2021.projmag.video

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import com.sdp13epfl2021.projmag.FORM_TO_SUBTITLE_MESSAGE
import com.sdp13epfl2021.projmag.R
import org.w3c.dom.Text

class VideoSubtitlingActivity : AppCompatActivity() {
    companion object {
        const val START = true
        const val END = true
    }

    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_subtitling)

        videoUri = intent.getStringExtra(FORM_TO_SUBTITLE_MESSAGE)?.let { Uri.parse(it) }
        handleVideoUri(videoUri)
    }

    fun handleVideoUri(uri: Uri?) {
        if (videoUri == null) {
            Log.d("video-sub", "if")
            Toast.makeText(
                this,
                getString(R.string.video_subtitling_cant_open_video),
                Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            Log.d("video-sub", "else")
            val mediaController = MediaController(this)
            val videoView = findViewById<VideoView>(R.id.video_subtitling_videoview)

            mediaController.setAnchorView(videoView)
            mediaController.setMediaPlayer(videoView)
            videoView.setVideoURI(videoUri)
            videoView.setMediaController(mediaController)
            videoView.start()
        }
    }

    fun setStartOrEnd(or: Boolean) {
        val videoView = findViewById<VideoView>(R.id.video_subtitling_videoview)
        val textView: TextView = if (or)
            findViewById(R.id.video_subtitling_start_time)
        else
            findViewById(R.id.video_subtitling_end_time)
        videoView.suspend()
        textView.text = videoView.currentPosition.toString()

    }
}