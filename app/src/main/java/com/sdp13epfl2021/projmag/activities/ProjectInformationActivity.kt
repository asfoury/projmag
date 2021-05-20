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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.sdp13epfl2021.projmag.MainActivity
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.database.interfaces.FileDatabase
import com.sdp13epfl2021.projmag.database.interfaces.MetadataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.UserdataDatabase
import com.sdp13epfl2021.projmag.database.interfaces.*
import com.sdp13epfl2021.projmag.model.Candidature
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Activity displaying the information and media of a project and from which
 * one can apply to the project or send it using a deep link or QR code.
 */
class ProjectInformationActivity : AppCompatActivity() {


    private lateinit var projectVar: ImmutableProject
    private lateinit var fileDB: FileDatabase
    private lateinit var metadataDB: MetadataDatabase
    private lateinit var videoView: VideoView
    private lateinit var descriptionView: TextView
    private lateinit var projectDir: File
    private lateinit var favButton: Button
    private val videosUris: MutableList<Pair<Uri, String?>> = ArrayList()
    private var current: Int = -1
    private var userId: String? = null
    private var alreadyApplied: Boolean = false
    private var appliedProjectsIds: MutableList<ProjectId> = ArrayList()
    private lateinit var userdataDatabase: UserdataDatabase
    private lateinit var candidatureDatabase: CandidatureDatabase

    private lateinit var candidatureListeners : HashMap<ProjectId, ListenerRegistration>


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

    /**
     * Function that given a boolean and strings set the value of the button according to the string
     * values. When the boolean is null it shows loading by default.
     *
     * @param button button to manipulate
     * @param isOn boolean value indicating the value of the boolean
     * @param trueText text to show on the button when the boolean value is true
     * @param falseText text to show on the button when the boolean value is false
     */
    private fun setButtonText(
            button: Button, isOn: Boolean?,
            trueText: String, falseText: String
    ) {
        button.text = when (isOn) {
            null -> {
                button.isEnabled = false
                getString(R.string.loading)
            }
            true -> {
                button.isEnabled = true
                trueText
            }
            false -> {
                button.isEnabled = true
                falseText
            }
        }
    }

    private fun handleChangesInStatusForAppliedProject(applyButton: Button, projectId: ProjectId) {
            val listener = candidatureDatabase.addListener(projectId) { _: ProjectId, list : List<Candidature> ->
            val candidatureThatChanged : Candidature? = list.find { candidature -> candidature.userId == userId }
            if(candidatureThatChanged != null) {
                if(candidatureThatChanged.state == Candidature.State.Accepted) {
                    Log.d("MYTEST", "You have been accepted to a project")
                    val otherCandidatures = appliedProjectsIds.filter { projectId -> (candidatureThatChanged.projectId != projectId) }
                    Log.d("MYTEST", "size of other = ${otherCandidatures.size}")
                    otherCandidatures.forEach { otherCandidatureId ->
                        candidatureDatabase.removeCandidature(
                                otherCandidatureId,
                                userId!!,
                                {
                                    showToast(getString(R.string.success), Toast.LENGTH_SHORT)
                                    setButtonText(applyButton, false,getString(R.string.unaply_text), getString(R.string.apply_text))
                                },
                                { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
                        )
                        userdataDatabase.applyUnapply(false, otherCandidatureId, {}, {})
                        Log.d("MYTEST", "Removed a project")
                    }
                } else if(candidatureThatChanged.state == Candidature.State.Rejected) {
                    Log.d("MYTEST", "A project you applied to is no longer available")
                } else {
                    Log.d("MYTEST", "Still on the waiting list")
                }
            }
        }

        candidatureListeners[projectId] = listener


    }


    private fun onApplyClick(
            applyButton: Button,
            candidatureDatabase: CandidatureDatabase,
            projectId: ProjectId,
    ) {
        if (alreadyApplied) {
            candidatureDatabase.removeCandidature(
                    projectId,
                    userId!!,
                    {
                        showToast(getString(R.string.success), Toast.LENGTH_SHORT)
                        alreadyApplied = !alreadyApplied
                        setButtonText(applyButton, alreadyApplied,getString(R.string.unaply_text), getString(R.string.apply_text))
                    },
                    { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
            )
            // remove the listener on the project because user no longer cares about the project
            val projectListener = candidatureListeners[projectId]
            if(projectListener != null) {
                projectListener.remove()
                candidatureListeners.remove(projectId)
            } else {
                Log.d("MYTEST","Listener is null in remove case")
            }

        } else {
            candidatureDatabase.pushCandidature(
                    projectId,
                    userId!!,
                    Candidature.State.Waiting,
                    {
                        showToast(getString(R.string.success), Toast.LENGTH_SHORT)
                        alreadyApplied = !alreadyApplied
                        setButtonText(applyButton, alreadyApplied,getString(R.string.unaply_text), getString(R.string.apply_text))
                    },
                    { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) }
            )
            // set up listener to listen to changes to the state of the candidature
            handleChangesInStatusForAppliedProject(applyButton, projectId)
        }

        for((k, v) in candidatureListeners) {
            Log.d("MYTEST", "Project id: $k")
        }

    }

    private fun setUpApplyButton(applyButton: Button) {
        val projectId = projectVar.id
        val utils = Utils.getInstance(this)
        userdataDatabase = utils.userdataDatabase
        candidatureDatabase = utils.candidatureDatabase
        setButtonText(applyButton, null,getString(R.string.unaply_text), getString(R.string.apply_text))
        userdataDatabase.getListOfAppliedToProjects({ projectIds ->
            appliedProjectsIds.addAll(projectIds)
            var alreadyApplied = projectIds.contains(projectId)
            setButtonText(applyButton, alreadyApplied,getString(R.string.unaply_text), getString(R.string.apply_text))
            applyButton.isEnabled = !projectVar.isTaken

        }, {})

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

    private fun setUpFavoritesButton() {
        val projectId = projectVar.id

        //until data is loaded from database, show loading
        setButtonText(
                favButton,
                null,
                getString(R.string.favorite_remove_button),
                getString(R.string.favorite_add_button)
        )

        //load data from the database and set the favorite add button to the right value
        handleFavoriteButtonText(projectId, false)


        //handle click on the add favorite button
        favButton.setOnClickListener {
            handleFavoriteButtonText(projectId, true)
        }


    }

    /**
     * Function that contacts the database, pushes/removes the project from the favorite list,
     * and changes the button value to the right value
     *
     * @param projectId : id of the project to push
     * @param isClick : differentiate the initialisation of the favorite button from a click on the button
     */
    private fun handleFavoriteButtonText(projectId: String, isClick: Boolean) {
        userdataDatabase.getListOfFavoriteProjects({ projectIds ->
            val isFavorite = projectIds.contains(projectId)
            //we clicked on the favorites button and the project is in the favorite list
            favButton.isEnabled = true
            if (isFavorite && isClick) {
                userdataDatabase.removeFromFavorite(projectId,
                        { onSuccessFavorite(isFavorite) },
                        { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) })
            }
            //clicked on the favorites button and the project isn't in the favorite list
            else if (isClick) {
                userdataDatabase.pushFavoriteProject(projectId,
                        { onSuccessFavorite(isFavorite) },
                        { showToast(getString(R.string.failure), Toast.LENGTH_SHORT) })
            }
            //initialize the button
            else {
                setButtonText(
                        favButton, isFavorite,
                        getString(R.string.favorite_remove_button),
                        getString(R.string.favorite_add_button)
                )
            }


        }, { showToast("failure to contact the database", Toast.LENGTH_SHORT) })
    }

    private fun onSuccessFavorite(isFavorite: Boolean) {
        showToast(getString(R.string.success), Toast.LENGTH_SHORT)
        setButtonText(
                favButton, !isFavorite,
                getString(R.string.favorite_remove_button),
                getString(R.string.favorite_add_button)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_information)

        val utils = Utils.getInstance(this)
        userId = utils.auth.currentUser?.uid
        fileDB = utils.fileDatabase
        metadataDB = utils.metadataDatabase
        userdataDatabase = utils.userdataDatabase

        // get all the text views that will be set
        val title = findViewById<TextView>(R.id.info_project_title)
        val lab = findViewById<TextView>(R.id.info_lab_name)
        descriptionView = findViewById(R.id.info_description)
        val nbOfStudents = findViewById<TextView>(R.id.info_nb_students)
        val type = findViewById<TextView>(R.id.info_available_for)
        val responsible = findViewById<TextView>(R.id.info_responsible_name)
        val creationDate = findViewById<TextView>(R.id.info_creation_date)
        videoView = findViewById(R.id.info_video)
        favButton = findViewById(R.id.addFavoriteInProject)
        favButton.isEnabled = false


        // get the project
        val project: ImmutableProject? = intent.getParcelableExtra(MainActivity.projectString)
        if (project != null) {
            projectVar = project
            // set the text views
            title.text = project.name
            lab.text = project.lab

            responsible.text = project.teacher

            nbOfStudents.text = getString(R.string.display_number_student, project.nbParticipant)
            creationDate.text = SimpleDateFormat(
                    getString(R.string.diplay_creation_date_format),
                    Locale.getDefault()
            ).format(project.creationDate)
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
                handleVideoWithFavoritePersistenceFiltering(project.videoURI)
            }
        } else {
            showToast("An error occurred while loading project.", Toast.LENGTH_LONG)
        }

        // make the back button in the title bar work
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setUpApplyButton(findViewById<Button>(R.id.applyButton))
        setUpFavoritesButton()

        Log.d("MYTEST","oncreate getting called")
        candidatureListeners = HashMap()

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
        controller.setPrevNextListeners({
            readNextVideo()
        }, {
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

    private fun handleVideoWithFavoritePersistenceFiltering(videosLinks: List<String>){
        //get the favorite list and when it's available provide it to the function that is
        //responsible for downloading videos in volatile or non volatile memory
        userdataDatabase.getListOfFavoriteProjects({ favorites ->
            addVideoAfterDownloadedWithFavoritePersistence(videosLinks, favorites.contains(projectVar.id))
        },{

        })
    }

    // download all videos and add them to the video player
    private fun addVideoAfterDownloadedWithFavoritePersistence(videosLinks: List<String>, isFavorite : Boolean ) {

        videosLinks.forEach { link ->
            if(isFavorite){//storing the video with persistence
                storingVideo(link, projectDir)
            }else {
                storingVideo(link, cacheDir)
            }
        }

    }



    private fun storingVideo(link : String, directory : File){
        fileDB.getFile(link, directory, { file ->
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

    /**
     * Creates menu with a share button and a QR code button.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_project_information, menu)
        return true
    }

    /**
     * QR code button opens QRCodeActivity and share button generates
     * a deep link and opens the android share activity.
     */
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
