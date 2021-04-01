package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI


class Form : AppCompatActivity() {
    private val REQUEST_VIDEO_ACCESS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val addVideoButton: Button = findViewById(R.id.add_video)
        addVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(intent, "Select Video"),
                REQUEST_VIDEO_ACCESS
            )
        }
    }


    /**
     * This function is called after the user comes back
     * from selecting a video from the file explorer
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_ACCESS) {
            if (data?.data != null) {
                val selectedVidURI = data.data
                //val pathInLocalStorage = selectedVidURI!!.path

                val playVidButton = findViewById<Button>(R.id.play_video)
                val vidView = findViewById<VideoView>(R.id.videoView)
                val mediaController = MediaController(this)

                FormHelper.playVideoFromLocalPath(
                    playVidButton,
                    vidView,
                    mediaController,
                    selectedVidURI!!
                )
            }
        }
    }
}

class FormHelper() {
    companion object {
        public fun playVideoFromLocalPath(
            playVidButton: Button,
            vidView: VideoView,
            mediaController: MediaController,
            uri: Uri
        ) {
            playVidButton.isEnabled = true
            playVidButton.setOnClickListener {
                vidView.setMediaController(mediaController)
                vidView.setVideoURI(uri)
                vidView.start()
            }
        }


    }
}
