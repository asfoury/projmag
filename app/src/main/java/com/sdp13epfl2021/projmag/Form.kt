package com.sdp13epfl2021.projmag

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sdp13epfl2021.projmag.database.ProjectUploader
import com.sdp13epfl2021.projmag.database.Utils
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Result

class Form : AppCompatActivity() {
    private fun initUi() {
        setContentView(R.layout.activity_form)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
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
     *  Get the temporary video uri
     *  Needed when uploading video from local storage to distant database
     */
    private fun getTmpVideoUri(): Uri? = null    /* TODO */


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
    fun submit(view: View) = Firebase.auth.uid?.let {
        ProjectUploader(
            Utils.projectsDatabase,
            it,
            FirebaseStorage.getInstance()
        ).checkProjectAndThenUpload(
            constructProject(),
            getTmpVideoUri(),
            ::showToast,
            ::finishFromOtherThread
        )
    }
}