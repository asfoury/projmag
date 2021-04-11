package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.activities.tagsSelectorActivity
import com.sdp13epfl2021.projmag.model.Tag


class Form : AppCompatActivity() {
    private val REQUEST_VIDEO_ACCESS = 1
    //private lateinit var tagAdapter : TagAdapter
    private lateinit var tagRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val addVideoButton: Button = findViewById(R.id.add_video)
        val addtagButton : Button = findViewById(R.id.addTagsButton)

        addVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(intent, "Select Video"),
                REQUEST_VIDEO_ACCESS
            )
        }



        addtagButton.setOnClickListener {
            switchToTagsSelectionActivity()
        }
    }

    /**
     * Function that initializes the tagAdapter and tagRecycler view
     * For now, will not update in live the tags from the tagsbase
     */
    fun createTagAddActivity(){
        setContentView(R.layout.activity_tags_selector)

        tagRecyclerView = findViewById<RecyclerView>(R.id.recycler_tag_view)
        //tagAdapter = TagAdapter(this, listOf(Tag("hello"), Tag("bye")))
        //tagRecyclerView.adapter = tagAdapter
        tagRecyclerView.setHasFixedSize(false)
    }

    /**
     * This function is called after the user comes back
     * from selecting a video from the file explorer
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_ACCESS) {
            if (data?.data != null) {
                // THIS IS THE VID URI
                val selectedVidURI = data.data


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


    fun switchToTagsSelectionActivity(){
        //why do i need to do the :: class.java to make it work
        System.out.println("helllloooooooooooooo")
        val intent = Intent(this, tagsSelectorActivity::class.java)
        startActivity(intent)
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
