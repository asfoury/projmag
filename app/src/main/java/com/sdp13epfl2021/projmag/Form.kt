package com.sdp13epfl2021.projmag

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.database.FirebaseFileDatabase
import com.sdp13epfl2021.projmag.database.ProjectUploader
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Result
import com.sdp13epfl2021.projmag.video.VideoSubtitlingActivity


const val FORM_TO_SUBTITLE_MESSAGE = "com.sdp13epfl2021.projmag.FROM_TO_SUBTITLE_MESSAGE"

class Form : AppCompatActivity() {
    companion object {
        private const val REQUEST_VIDEO_ACCESS = 1
        private const val REQUEST_VIDEO_SUBTITLING = 2
    }

    private var videoUri: Uri? = null
    private var subtitles: String? = null

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

        findViewById<TextView>(R.id.title_form)?.requestFocus()
        findViewById<Button>(R.id.form_add_subtitle)?.setOnClickListener(::onClickSubtitleButton)
        findViewById<Button>(R.id.form_button_sub)?.setOnClickListener(::submit)
    }


    /**
     * Disable submission button
     * Useful to submit only one project at time
     */
    private fun setSubmitButtonEnabled(enabled: Boolean) = runOnUiThread {
        findViewById<Button>(R.id.form_button_sub)?.apply {
            isEnabled = enabled
            text = if (enabled) getString(R.string.submission) else getString(R.string.loading)
        }
    }

    private fun onClickSubtitleButton(view: View) {
        val intent = Intent(this, VideoSubtitlingActivity::class.java).apply {
            putExtra(FORM_TO_SUBTITLE_MESSAGE, videoUri.toString())
        }
        startActivityForResult(
            intent,
            REQUEST_VIDEO_SUBTITLING
        )
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
                videoUri = data.data

                val playVidButton = findViewById<Button>(R.id.play_video)
                val subtitleButton = findViewById<Button>(R.id.form_add_subtitle)
                val vidView = findViewById<VideoView>(R.id.videoView)
                val mediaController = MediaController(this)

                FormHelper.playVideoFromLocalPath(
                    playVidButton,
                    subtitleButton,
                    vidView,
                    mediaController,
                    videoUri!!
                )
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_SUBTITLING) {
            if (data != null) {
                subtitles = data.getStringExtra(VideoSubtitlingActivity.RESULT_KEY)
            }
        }
    }

    /**
     * Extract string text content form an EditText view
     */
    private fun getTextFromEditText(id: Int): String = findViewById<EditText>(id).run {
        text.toString()
    }

    /**
     * Show a toast message on the UI thread
     * This is useful when using async callbacks
     */
    private fun showToast(msg: String) = runOnUiThread {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Construct a Project with data present in the view
     */
    private fun constructProject(): Result<ImmutableProject> {
        return ImmutableProject.build(
            id = "",
            name = getTextFromEditText(R.id.form_edit_text_project_name),
            lab = getTextFromEditText(R.id.form_edit_text_laboratory),
            teacher = getTextFromEditText(R.id.form_edit_text_teacher),
            TA = getTextFromEditText(R.id.form_edit_text_project_TA),
            nbParticipant = try {
                getTextFromEditText(R.id.form_nb_of_participant).toInt()
            } catch (_: NumberFormatException) {
                0
            },
            masterProject = findViewById<CheckBox>(R.id.form_check_box_MP).isChecked,
            bachelorProject = findViewById<CheckBox>(R.id.form_check_box_SP).isChecked,
            isTaken = false,
            description = getTextFromEditText(R.id.form_project_description),
            assigned = listOf(),
            tags = listOf("Default-tag")
        )
    }


    /**
     * Finish the activity from another thread
     * Useful when using async callbacks
     */
    private fun finishFromOtherThread() = runOnUiThread {
        finish()
    }

    /**
     * Submit project and video with information in the view.
     * Expected to be called when clicking on a submission button on the view
     */
    private fun submit(view: View) = Firebase.auth.uid?.let {
        setSubmitButtonEnabled(false) // disable submit, as there is a long time uploading video
        val utils = Utils(this)
        ProjectUploader(
            utils.projectsDatabase,
            utils.fileDatabase,
            utils.metadataDatabase,
            ::showToast,
            { setSubmitButtonEnabled(true) },
            ::finishFromOtherThread
        ).checkProjectAndThenUpload(
            constructProject(),
            videoUri,
            subtitles
        )
    }
}


object FormHelper {
    fun playVideoFromLocalPath(
        playVidButton: Button,
        subtitleButton: Button,
        vidView: VideoView,
        mediaController: MediaController,
        uri: Uri
    ) {
        playVidButton.isEnabled = true
        playVidButton.setOnClickListener {
            vidView.setMediaController(mediaController)
            vidView.setVideoURI(uri)
            vidView.start()
            vidView.visibility = VISIBLE
        }
        subtitleButton.isEnabled = true
    }
}

