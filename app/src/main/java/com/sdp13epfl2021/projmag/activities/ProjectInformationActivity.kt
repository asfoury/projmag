package com.sdp13epfl2021.projmag.activities


import android.annotation.SuppressLint
import android.content.Context
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
import android.view.MotionEvent
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.FileDatabase
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ImmutableProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xml.sax.XMLReader
import java.io.File

class ProjectInformationActivity : AppCompatActivity() {

    private lateinit var fileDB: FileDatabase
    private lateinit var videoView: VideoView
    private val videosUris: MutableList<Uri> = ArrayList()
    private var current: Int = -1

    @Synchronized
    private fun addVideo(videoUri: Uri) {
        videosUris.add(videoUri)
        if (current < 0) {
            readNextVideo()
            videoView.isInvisible = false
            videoView.pause()
        }
    }

    private fun readNextVideo() {
        if (current + 1 < videosUris.size) {
            current++
            videoView.setVideoURI(videosUris[current])
            videoView.start()
        }
    }

    private fun readPrevVideo() {
        if (current > 0) {
            current--
            videoView.setVideoURI(videosUris[current])
            videoView.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        fileDB = Utils(this).fileDatabase

        // get all the text views that will be set
        val title = findViewById<TextView>(R.id.info_project_title)
        val lab = findViewById<TextView>(R.id.info_lab_name)
        val description = findViewById<TextView>(R.id.info_description)
        val nbOfStudents = findViewById<TextView>(R.id.info_nb_students)
        val type = findViewById<TextView>(R.id.info_available_for)
        val responsible = findViewById<TextView>(R.id.info_responsible_name)
        videoView = findViewById<VideoView>(R.id.info_video)

        // get the project
        val project: ImmutableProject? = intent.getParcelableExtra("project")
        if (project != null) {
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

            val projectDir = File(File(filesDir, "projects"), project.id)
            val (cleanDescription, videosLinks) = extractVideos(project.description)

            setupDescriptionWithHTML(description, cleanDescription, projectDir)

            videoView.isInvisible = true // hide the videoView before a video is loaded
            if (videosLinks.isNotEmpty()) {
                val controller = MediaController(this)

                addPauseOnTouchListener(controller)
                setupPlayerListeners(controller)
                addVideoAfterDownloaded(videosLinks, projectDir)
            }
        } else {
            showToast("An error occurred while loading project.")
        }

        // make the back button in the title bar work
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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
    private fun addVideoAfterDownloaded(videosLinks: List<String>, projectDir: File) {
        videosLinks.forEach { link ->
            fileDB.getFile(link, projectDir, { file ->
                addVideo(Uri.fromFile(file))
            }, {
                showToast("An error occurred while downloading a video.")
            })
        }
    }

    private fun setupDescriptionWithHTML(description: TextView, cleanDescription: String, projectDir: File) {
        val imageGetter = ImageGetter2(this, projectDir, description)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.text = Html.fromHtml(
                cleanDescription,
                Html.FROM_HTML_MODE_LEGACY,
                imageGetter,
                TagHandler2()
            )
        } else {
            description.text = Html.fromHtml(cleanDescription, imageGetter, TagHandler2())
        }

        // Allow user to click on link
        description.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showToast(error: String) {
        runOnUiThread {
            Toast.makeText(
                this,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

     // return a pair of (text without video tag, and a list of theses videos links)
    private fun extractVideos(text: CharSequence): Pair<String, List<String>> {
        var newText = text
        var links = emptyList<String>()

        val startString = "<video>"
        val endString = "</video>"

        while (newText.isNotEmpty()) {
            val start = newText.indexOf(startString)
            val end = newText.indexOf(endString)
            if (start < 0 || end < 0) {
                break
            }
            links = links + newText.substring(start + startString.length, end)
            newText = newText.removeRange(start, end + endString.length)
        }

        return Pair(newText.toString(), links)
    }

    private inner class ImageGetter2(
        private val context: Context,
        private val projectDir: File,
        private val htmlTextView: TextView
    ) : Html.ImageGetter {

        override fun getDrawable(source: String): Drawable {
            val res = context.resources
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
                            htmlTextView.text = htmlTextView.text
                        }
                    } else {
                        showToast("An error occurred while loading image.")
                    }
                }
            }, {
                showToast("An error occurred while downloading image.")
            })
            return holder
        }
    }

    private class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) : BitmapDrawable(res, bitmap) {
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
}