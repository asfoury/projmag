package com.sdp13epfl2021.projmag.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.curriculumvitae.CurriculumVitae
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.database.interfaces.CandidatureDatabase
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.ProjectId
import com.sdp13epfl2021.projmag.model.Candidature
import com.sdp13epfl2021.projmag.model.ImmutableProfile
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.video.VideoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.glxn.qrgen.android.QRCode
import org.xml.sax.XMLReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ProjectInformationActivity : AppCompatActivity() {

    companion object {
        val LOADING_STRING = "LOADING"
        val APPLY_STRING = "APPLY"
        val UNAPPLY_STRING = "UNAPPLY"
    }

    private lateinit var projectVar: ImmutableProject
    private lateinit var fileDB: FileDatabase
    private lateinit var metadataDB: MetadataDatabase
    private lateinit var videoView: VideoView
    private lateinit var descriptionView: TextView
    private lateinit var projectDir: File
    private val videosUris: MutableList<Pair<Uri, String?>> = ArrayList()
    private var current: Int = -1
    private var userId: String? = null
    private var alreadyApplied: Boolean = false

    @Synchronized
    private fun addVideo(videoUri: Uri, subtitle: String?) {
        videosUris.add(Pair(videoUri, subtitle))
        if (current < 0) {
            readNextVideo()
            videoView.isInvisible = false
            videoView.pause()
        }
    }

    private fun readNextVideo() {
        if (current + 1 < videosUris.size) {
            current++
            updateVideoState()
        }
    }

    private fun readPrevVideo() {
        if (current > 0) {
            current--
            updateVideoState()
        }
    }

    private fun updateVideoState() {
        videoView.setVideoURI(videosUris[current].first)
        videosUris[current].second?.let {
            videoView.addSubtitleSource(
                it.byteInputStream(),
                VideoUtils.ENGLISH_WEBVTT_SUBTITLE_FORMAT
            )
        }
        videoView.start()
    }

    private fun setApplyButtonText(applyButton: Button, applied: Boolean?) {
        applyButton.text = when (applied) {
            null -> LOADING_STRING
            true -> UNAPPLY_STRING
            false -> APPLY_STRING
        }
    }

    private fun onApplyClick(applyButton: Button, candidatureDatabase: CandidatureDatabase, projectId: ProjectId, ) {
        if (alreadyApplied) {
            candidatureDatabase.removeCandidature(
                projectId,
                userId!!,
                {
                    showToast(getString(R.string.success), Toast.LENGTH_SHORT)
                    alreadyApplied = !alreadyApplied
                    setApplyButtonText(applyButton, alreadyApplied)
                },
                { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
            )
        } else {
            val candidature = Candidature(
                projectId,
                userId!!,
                ImmutableProfile.EMPTY_PROFILE,
                CurriculumVitae.EMPTY_CV,
                Candidature.State.Waiting
            )
            candidatureDatabase.pushCandidature(
                candidature,
                Candidature.State.Waiting,
                {
                    showToast(getString(R.string.success), Toast.LENGTH_SHORT)
                    alreadyApplied = !alreadyApplied
                    setApplyButtonText(applyButton, alreadyApplied)
                },
                { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
            )
        }
    }

    private fun setUpApplyButton(applyButton: Button) {
        val projectId = projectVar.id
        val utils = Utils.getInstance(this)
        val userdataDatabase = utils.userdataDatabase
        val candidatureDatabase = utils.candidatureDatabase
        setApplyButtonText(applyButton, null)
        userdataDatabase.getListOfAppliedToProjects({ projectIds ->
            alreadyApplied = projectIds.contains(projectId)
            setApplyButtonText(applyButton, alreadyApplied)
        }, {})

        applyButton.isEnabled = !projectVar.isTaken

        applyButton.setOnClickListener {
            userdataDatabase.applyUnapply(
                !alreadyApplied,
                projectId,
                {
                    if (userId != null) {
                        onApplyClick(applyButton, candidatureDatabase, projectId)
                    } else {
                        showToast(getString(R.string.failure), Toast.LENGTH_SHORT)
                    }
                },
                { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
            )

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        val utils = Utils.getInstance(this)
        userId = utils.auth.currentUser?.uid
        fileDB = utils.fileDatabase
        metadataDB = utils.metadataDatabase

        // get all the text views that will be set
        val title = findViewById<TextView>(R.id.info_project_title)
        val lab = findViewById<TextView>(R.id.info_lab_name)
        descriptionView = findViewById(R.id.info_description)
        val nbOfStudents = findViewById<TextView>(R.id.info_nb_students)
        val type = findViewById<TextView>(R.id.info_available_for)
        val responsible = findViewById<TextView>(R.id.info_responsible_name)
        videoView = findViewById(R.id.info_video)


        // get the project
        val project: ImmutableProject? = intent.getParcelableExtra(MainActivity.projectString)
        if (project != null) {
            projectVar = project
            // set the text views
            title.text = project.name
            lab.text = project.lab

            responsible.text = project.teacher

            nbOfStudents.text = getString(R.string.display_number_student, project.nbParticipant)
            type.text =
                if (project.bachelorProject && project.masterProject) getString(R.string.display_bachelor_and_master)
                else if (project.bachelorProject) getString(R.string.display_bachelor_only)
                else if (project.masterProject) getString(R.string.display_master_only)
                else getString(R.string.display_not_found)

            projectDir = File(File(filesDir, "projects"), project.id)

            setupDescriptionWithHTML(project.description)

            videoView.isInvisible = true // hide the videoView before a video is loaded
            if (project.videoURI.isNotEmpty()) {
                val controller = MediaController(this)

                addPauseOnTouchListener(controller)
                setupPlayerListeners(controller)
                addVideoAfterDownloaded(project.videoURI)
            }
        } else {
            showToast("An error occurred while loading project.", Toast.LENGTH_LONG)
        }

        // make the back button in the title bar work
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setUpApplyButton(findViewById<Button>(R.id.applyButton))
    }

    // pause/start when we touch the video
    @SuppressLint("ClickableViewAccessibility")
    private fun addPauseOnTouchListener(controller: MediaController) {
        videoView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (current >= 0) {
                        if (videoView.isPlaying) {
                            videoView.pause()
                        } else {
                            videoView.start()
                        }
                        controller.show() //update controller state
                    }
                }
            }
            true
        }
    }

    private fun setupPlayerListeners(controller: MediaController) {
        videoView.setMediaController(controller)

        // set the controller buttons to play next/prev video
        controller.setPrevNextListeners({ next ->
            readNextVideo()
        }, { prev ->
            readPrevVideo()
        })

        // play the next video when the video is finished
        videoView.setOnCompletionListener {
            readNextVideo()
        }

        // resize the video view to use full width (with original ratio)
        videoView.setOnPreparedListener { mp ->
            val width = resources.displayMetrics.widthPixels
            val height = (mp.videoHeight / (mp.videoWidth / (width).toFloat())).toInt()
            videoView.layoutParams.height = height
        }
    }

    // download all videos and add them to the video player
    private fun addVideoAfterDownloaded(videosLinks: List<String>) {
        videosLinks.forEach { link ->
            fileDB.getFile(link, projectDir, { file ->
                val uri = Uri.fromFile(file)
                metadataDB.getSubtitlesFromVideo(
                    link,
                    Locale.ENGLISH.language,
                    { subs ->
                        subs?.let {
                            addVideo(uri, it)
                        } ?: run { addVideo(uri, null) }
                    }, { addVideo(uri, null) }
                )
            }, { showToast(getString(R.string.could_not_download_video), Toast.LENGTH_LONG) })
        }
    }

    private fun setupDescriptionWithHTML(cleanDescription: String) {
        val imageGetter = ImageGetter2(resources, projectDir)
        val tagHandler = TagHandler2()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descriptionView.text = Html.fromHtml(
                cleanDescription,
                Html.FROM_HTML_MODE_LEGACY,
                imageGetter,
                tagHandler
            )
        } else {
            descriptionView.text = Html.fromHtml(cleanDescription, imageGetter, tagHandler)
        }

        // Allow user to click on link
        descriptionView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showToast(message: String, toastLength: Int) {
        runOnUiThread {
            Toast.makeText(
                this,
                message,
                toastLength
            ).show()
        }
    }


    private inner class ImageGetter2(
        private val res: Resources,
        private val projectDir: File
    ) : Html.ImageGetter {

        override fun getDrawable(source: String): Drawable {
            val holder = BitmapDrawablePlaceHolder(res, null)

            fileDB.getFile(source, projectDir, { file ->
                GlobalScope.launch(Dispatchers.IO) {
                    val draw = Drawable.createFromPath(file.path)
                    if (draw != null) {
                        var width = draw.intrinsicWidth
                        var height = draw.intrinsicHeight

                        val maxWidth = res.displayMetrics.widthPixels
                        if (width > maxWidth) {
                            height = (height / (width / maxWidth.toFloat())).toInt()
                            width = maxWidth
                        }

                        draw.setBounds(0, 0, width, height)
                        holder.setDrawable(draw)
                        holder.setBounds(0, 0, width, height)

                        // force view update
                        withContext(Dispatchers.Main) {
                            descriptionView.text = descriptionView.text
                        }
                    } else {
                        showToast("An error occurred while loading image.", Toast.LENGTH_LONG)
                    }
                }
            }, {
                showToast("An error occurred while downloading image.", Toast.LENGTH_LONG)
            })
            return holder
        }
    }

    private class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) : BitmapDrawable(
        res,
        bitmap
    ) {
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }

    // This is used to handle some tags unsupported in version 23 (Android 6.0), such as list ul/ol
    private class TagHandler2 : Html.TagHandler {

        var parent: String? = null
        var index: Int = 1

        override fun handleTag(
            opening: Boolean,
            tag: String,
            output: Editable,
            xmlReader: XMLReader
        ) {
            when (tag) {
                "ul", "ol" -> parent = if (opening) tag else null
                "li" -> {
                    if (opening) {
                        if (parent == "ul")
                            output.append("\n\t•")
                        if (parent == "ol") {
                            output.append("\n\t$index. ")
                            index += 1
                        }
                    }
                }
            }
        }
    }

    private fun createDynamicLink(): Uri {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/projectid=" + projectVar.id)
            domainUriPrefix = "https://projmag.page.link/"
            androidParameters {}
        }
        return dynamicLink.uri
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.waitingListButton)?.isVisible = (userId == projectVar.authorId)
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_project_information, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shareButton) {
            val linkToSend = createDynamicLink()

            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, linkToSend.toString())
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
            return true
        } else if (item.itemId == R.id.waitingListButton) {
            if (userId == projectVar.authorId) {
                val intent = Intent(this, WaitingListActivity::class.java)
                intent.putExtra(MainActivity.projectIdString, projectVar.id)
                startActivity(intent)
            } else {
                //this should not happen, unless the user was disconnected after loading the project view
                showToast(resources.getString(R.string.waiting_not_allowed), Toast.LENGTH_LONG)
            }
        } else if (item.itemId == R.id.generateQRCodeButton) {
            val linkToSend = createDynamicLink()

            val qrImage = QRCode.from(linkToSend.toString()).withSize(800, 800)
            val stream = ByteArrayOutputStream()
            qrImage.bitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            val intent = Intent(this, QRCodeActivity::class.java)
            intent.putExtra("qrcode", byteArray)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
