package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.R


class ProfilePageActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var buttonAddCv: Button
    lateinit var buttonSubChange: Button
    private val pickImage = 0
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        imageView = findViewById(R.id.image_profile)
        button = findViewById(R.id.button_edit_profile)
        buttonAddCv = findViewById(R.id.button_add_cv)
        buttonSubChange = findViewById(R.id.buttonSubChangeProfil)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        buttonAddCv.setOnClickListener{
            val intent = Intent(this,CVCreationActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonSubChange.setOnClickListener{
            val intent = Intent(this,ProjectsListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }
}