package com.sdp13epfl2021.projmag

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp13epfl2021.projmag.database.FirebaseProjectsDatabase
import com.sdp13epfl2021.projmag.model.Failure
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.Result
import com.sdp13epfl2021.projmag.model.Success

class Form : AppCompatActivity() {
    private fun initUi() {
        setContentView(R.layout.activity_form)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
    }

    private fun getTextFromView(id: Int): String = findViewById<EditText>(id).run {
        text.toString()
    }

    private fun showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

    private fun constructProject(): Result<ImmutableProject> {
        return ImmutableProject.build(
            name = getTextFromView(R.id.form_edit_text_project_name),
            lab = getTextFromView(R.id.form_edit_text_laboratory),
            teacher = getTextFromView(R.id.form_edit_text_teacher),
            TA = getTextFromView(R.id.form_edit_text_project_TA),
            nbParticipant = try {
                getTextFromView(R.id.form_nb_of_participant).toInt()
            } catch (_: NumberFormatException) {
                0
            },
            masterProject = findViewById<CheckBox>(R.id.form_check_box_MP).isChecked,
            bachelorProject = findViewById<CheckBox>(R.id.form_check_box_SP).isChecked,
            isTaken = false,
            description = getTextFromView(R.id.form_project_description),
            assigned = listOf(),
            tags = listOf("Default-tag")
        )
    }

    private fun sendToFirebase(project: Result<ImmutableProject>) =
        when (project) {
            is Success<*> -> {
                FirebaseProjectsDatabase(FirebaseFirestore.getInstance()).pushProject(
                    project.value as ImmutableProject,
                    { id -> showToast("Project pushed with ID : $id") },
                    { showToast("Can't push project") }
                )
            }
            is Failure<*> -> {
                Toast.makeText(this, project.reason, Toast.LENGTH_LONG).show()
            }
        }

    fun submit(view: View) = sendToFirebase(constructProject())


}