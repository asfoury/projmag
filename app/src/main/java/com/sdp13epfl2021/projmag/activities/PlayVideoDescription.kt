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

        // get the path of the video in the local storage
        val vidPath = intent.getStringExtra("videoPath")

        // get the video view and set the path of the video to play and start it
        val vidView = findViewById<VideoView>(R.id.videoView)
        vidView.setVideoPath(vidPath)
        vidView.start()
    }
}