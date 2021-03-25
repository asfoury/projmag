package com.sdp13epfl2021.projmag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import com.sdp13epfl2021.projmag.R

class PlayVideoDescription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video_description)
        val vidPath = intent.getStringExtra("videoPath")

        val vidView = findViewById<VideoView>(R.id.videoView)
        vidView.setVideoPath(vidPath)
        vidView.start()
    }
}