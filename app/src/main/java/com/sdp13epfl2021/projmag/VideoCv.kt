package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class VideoCv : AppCompatActivity() {
    lateinit var vidButton: Button
    lateinit var videoView: VideoView
    private var VideoUri: Uri? = null
    private val pickVideo = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_cv)
        videoView = findViewById(R.id.videoViewCv)
        vidButton = findViewById(R.id.videoButtonCV)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        vidButton.setOnClickListener {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(intent, pickVideo)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == pickVideo) {
            if (intent?.data != null) {
                VideoUri = intent?.data
                videoView.setVideoURI(VideoUri)
                videoView.start();
            }
        }
    }


}