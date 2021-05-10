package com.sdp13epfl2021.projmag.curriculumvitae

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
import com.sdp13epfl2021.projmag.R


class VideoCv : AppCompatActivity() {

    companion object {
        const val RESULT_KEY = "com.sdp13epfl2021.projmag.curriculumvitea.VideoCV"
    }

    lateinit var vidButton: Button
    lateinit var subButton: Button
    lateinit var videoView: VideoView
    private var videoUri: Uri? = null
    private val pickVideo = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_cv)
        videoView = findViewById(R.id.videoViewCv)
        vidButton = findViewById(R.id.videoButtonCV)
        subButton = findViewById(R.id.videoButtonCVSub)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        vidButton.setOnClickListener {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(intent, pickVideo)
            }
        }
        subButton.setOnClickListener {
            if (videoUri != null) {
                val data = Intent()
                data.putExtra(RESULT_KEY, videoUri.toString())
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == pickVideo) {
            if (intent?.data != null) {
                videoUri = intent.data
                videoView.setVideoURI(videoUri)
                videoView.start()
            }
        }
    }


}
