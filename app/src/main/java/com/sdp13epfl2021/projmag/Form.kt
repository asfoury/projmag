package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sdp13epfl2021.projmag.activities.PlayVideoDescription
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest


class Form : AppCompatActivity() {
    val REQUEST_VIDEO_ACCESS = 1

    private fun initUi() {
        setContentView(R.layout.activity_form)
    }


    private fun openGalleryForVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent,"Select Video"), REQUEST_VIDEO_ACCESS)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        val addVideoButton : Button = findViewById(R.id.add_video)
        addVideoButton.setOnClickListener {
            openGalleryForVideo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_ACCESS) {
            if(data?.data != null) {
                val selectedVidURI = data.data
                val selectedVidPath = getPath(selectedVidURI)
                val file  = File(selectedVidPath)

                // path of video in local storage unable to play video from it for now playing using location in external storage
                val pathInLocalStorage = saveVideoToLocalStorage(file)

                Log.d("PATH IN LOC",pathInLocalStorage)

                val playVidButton = findViewById<Button>(R.id.play_video)
                playVidButton.isEnabled = true
                playVidButton.setOnClickListener {
                    val intent = Intent(this, PlayVideoDescription::class.java)
                    intent.putExtra("videoPath",pathInLocalStorage)
                    startActivity(intent)
                }
            }
        }
    }




    private fun saveVideoToLocalStorage(file : File) : String {
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(file.name, Context.MODE_PRIVATE)
            fileOutputStream.write(file.readBytes())
            fileOutputStream.close()

        } catch (e: Exception){
            e.printStackTrace()
        }

      return "${this.filesDir}/${file.name}"
    }

    private fun getPath(uri: Uri?): String? {
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

    private fun playVideoInDevicePlayer(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
        startActivity(intent)
    }

}
