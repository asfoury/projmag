package com.sdp13epfl2021.projmag

import android.content.Intent
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
           val gallery = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickVideo)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickVideo) {
            if (data?.data != null) {
                val uriPathHelper = URIPathHelper()
                val videoPath = uriPathHelper.getPath(this, data?.data!!)
                VideoUri = data?.data
                videoView.setVideoURI(VideoUri)
                videoView.start();
            }
        }
    }

}