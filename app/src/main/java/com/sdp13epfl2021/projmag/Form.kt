package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.activities.PlayVideoDescription
import java.io.File
import java.io.FileOutputStream


class Form : AppCompatActivity() {
    val REQUEST_VIDEO_ACCESS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        val addVideoButton: Button = findViewById(R.id.add_video)
        addVideoButton.setOnClickListener {
            openGalleryForVideo()
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
                val selectedVidPath = getPath(selectedVidURI)
                Log.d("GET PATH", "After: ${selectedVidPath}")

                val file = File(selectedVidPath)

                // path of video in local storage unable to play video from it for now playing using location in external storage
                val pathInLocalStorage = saveVideoToLocalStorage(file)

                Log.d("PATH IN LOC", pathInLocalStorage)

                val playVidButton = findViewById<Button>(R.id.play_video)
                playVidButton.isEnabled = true
                playVidButton.setOnClickListener {
                    val intent = Intent(this, PlayVideoDescription::class.java)
                    intent.putExtra("videoPath", pathInLocalStorage)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * Initialises the Form UI
     */
    private fun initUi() {
        setContentView(R.layout.activity_form)
    }

    /**
     * Opens the file explorer so the user
     * can select a video from the device
     */
    private fun openGalleryForVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_VIDEO_ACCESS)

    }

    /**
     * Saves the video selected by the user to the
     * apps local storage by keeping the name of the file in
     * the gallery, if same video is selected no new videos are saved
     */
    private fun saveVideoToLocalStorage(file: File): String {
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(file.name, Context.MODE_PRIVATE)
            fileOutputStream.write(file.readBytes())
            fileOutputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "${this.filesDir}/${file.name}"
    }


    /**
     * Takes the URI returned from the intent and gets the
     * absolute location of the video that has been selected
     * this is needed to be able to copy the video file that has
     * been selected, for now uses a deprecated variable till
     * we find the correct way of implementing it
     */
    private fun getPath(uri: Uri?): String? {
        Log.d("GET PATH", "Before: ${uri.toString()}")
        val projection =
            arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } else null
    }
}
