package com.sdp13epfl2021.projmag.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.ProjectUploader
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectDatabase
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Result
import com.sdp13epfl2021.projmag.video.VideoUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


const val FORM_TO_SUBTITLE_MESSAGE = "com.sdp13epfl2021.projmag.activities.FORM_TO_SUBTITLE_MESSAGE"

/**
 * Activity consisting of a form one can use to create and submit a project.
 */
@AndroidEntryPoint
class ProjectCreationActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_VIDEO_ACCESS = 1
        private const val REQUEST_VIDEO_SUBTITLING = 2
        private const val REQUEST_TAG_ACCESS = 3
        private const val REQUEST_SELECTION_ACCESS = 4
        const val EDIT_EXTRA = "edit"
    }

    private var projectToEdit: ImmutableProject? = null
    private var changedVid = false

    @Inject
    lateinit var projectDB: ProjectDatabase

    @Inject
    lateinit var fileDB: FileDatabase

    @Inject
    lateinit var metadataDB: MetadataDatabase

    @Inject
    lateinit var candidatureDB: CandidatureDatabase

    @Inject
    @Named("currentUserId")
    lateinit var userID: String

    //tag selection related variables
    private lateinit var tagRecyclerView: RecyclerView

    private var videoUri: Uri? = null
    private var subtitles: String? = null
    private var listTags: Array<String> = emptyArray()
    private var listSections: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_creation)
        projectToEdit = intent.getParcelableExtra(EDIT_EXTRA) as ImmutableProject?

        setUpButtons()

        projectToEdit?.let {
            setInitialValues(it)
        }
    }

    private fun setUpButtons() {
        val addVideoButton: Button = findViewById(R.id.add_video)
        val addTagButton: Button = findViewById(R.id.addTagsButton)
        val addSectionButton: Button = findViewById(R.id.addSectionButton)
        addVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(intent, "Select Video"),
                REQUEST_VIDEO_ACCESS
            )
        }

        findViewById<Button>(R.id.form_button_sub)?.setOnClickListener(::submit)
        addTagButton.setOnClickListener {
            switchToTagsSelectionActivity()
        }
        addSectionButton.setOnClickListener {
            switchToSectionSelectionActivity()
        }
        findViewById<TextView>(R.id.title_form).requestFocus()
        findViewById<Button>(R.id.form_add_subtitle).setOnClickListener(::onClickSubtitleButton)
        findViewById<Button>(R.id.form_button_sub).setOnClickListener(::submit)
    }

    private fun setInitialValues(project: ImmutableProject) {
        findViewById<TextView>(R.id.form_edit_text_project_name).text = project.name
        findViewById<TextView>(R.id.form_edit_text_laboratory).text = project.lab
        findViewById<TextView>(R.id.form_edit_text_teacher).text = project.teacher
        findViewById<TextView>(R.id.form_edit_text_project_TA).text = project.TA
        findViewById<TextView>(R.id.form_nb_of_participant).text = project.nbParticipant.toString()
        findViewById<CheckBox>(R.id.form_check_box_SP).isChecked = project.bachelorProject
        findViewById<CheckBox>(R.id.form_check_box_MP).isChecked = project.masterProject
        findViewById<TextView>(R.id.form_project_description).text = project.description
        listTags = project.tags.toTypedArray()
        listSections = project.allowedSections.toTypedArray()
        if (!project.videoUri.isEmpty()) {
            videoUri = Uri.parse(project.videoUri[0])
            setUpVideo()
        }
    }

    /**
     * Disable submission button
     * Useful to submit only one project at time
     */
    private fun setSubmitButtonEnabled(enabled: Boolean) = runOnUiThread {
        findViewById<Button>(R.id.form_button_sub).apply {
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

    private fun setUpVideo() {
        val vidView = findViewById<VideoView>(R.id.videoView)
        val playVidButton = findViewById<Button>(R.id.play_video)
        val subtitleButton = findViewById<Button>(R.id.form_add_subtitle)
        val mediaController = MediaController(this)

        FormHelper.playVideoFromLocalPath(
            playVidButton,
            subtitleButton,
            vidView,
            mediaController,
            videoUri!!
        )
    }

    /**
     * This function is called after the user comes back
     * from selecting a video from the file explorer
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val vidView = findViewById<VideoView>(R.id.videoView)
        changedVid = true
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_ACCESS) {
            if (data?.data != null) {
                // THIS IS THE VID URI
                videoUri = data.data

                setUpVideo()
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_SUBTITLING) {
            if (data != null) {
                data.getStringExtra(VIDEO_SUBTITLING_ACTIVITY_RESULT_KEY)?.let {
                    subtitles = it
                    vidView.addSubtitleSource(
                        it.byteInputStream(),
                        VideoUtils.ENGLISH_WEBVTT_SUBTITLE_FORMAT
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAG_ACCESS) {
            if (data != null) {
                val tagData = data.getStringArrayExtra(MainActivity.tagsList)
                if (tagData != null) {
                    listTags = tagData
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECTION_ACCESS) {
            if (data != null) {
                val secList = data.getStringArrayExtra(MainActivity.sectionsList)
                if (secList != null) {
                    listSections = secList
                }
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
            id = if (projectToEdit == null) "" else projectToEdit!!.id, //id is defined by firebase itself
            name = getTextFromEditText(R.id.form_edit_text_project_name),
            lab = getTextFromEditText(R.id.form_edit_text_laboratory),
            authorId = userID,
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
            tags = listTags.toList(),
            allowedSections = listSections.toList(),
            videoURI = if (videoUri != null) listOf(videoUri.toString()) else listOf()
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
    private fun submit(view: View) {
        setSubmitButtonEnabled(false) // disable submit, as there is a long time uploading video
        ProjectUploader(
            projectDB,
            fileDB,
            metadataDB,
            candidatureDB,
            ::showToast,
            { setSubmitButtonEnabled(true) },
            ::finishFromOtherThread
        ).checkProjectAndThenUpload(
            constructProject(),
            if (changedVid) videoUri else null,
            subtitles
        )
    }

    /**
     * Switch to the tag selection activity that will then comeback to this activity
     * And update the project list if the activity finishes properly
     */
    private fun switchToTagsSelectionActivity() {
        //why do i need to do the :: class.java to make it work
        val intent = Intent(this, TagsSelectorActivity::class.java)
        startActivityForResult(intent, REQUEST_TAG_ACCESS)

    }

    private fun switchToSectionSelectionActivity() {
        val intent = Intent(this, SectionSelectionActivity::class.java)
        startActivityForResult(intent, REQUEST_SELECTION_ACCESS)
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


