package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.R

class UserTypeChoice : AppCompatActivity() {

    companion object{
        //user is Professor by default
        var isProfessor: Boolean = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type_choice)
        val professorButton: RadioButton = findViewById(R.id.radioProfessorType)
        val studentButton: RadioButton= findViewById(R.id.radioStudentType)
        val pdhButton: RadioButton= findViewById(R.id.radioPHDType)
        val intent = Intent(this, ProfilePageActivity::class.java)
        pdhButton.setOnClickListener{
            isProfessor = true
            startActivity(intent)
        }
        professorButton.setOnClickListener{
            isProfessor = true
            startActivity(intent)
        }
        studentButton.setOnClickListener{
            isProfessor = false
            startActivity(intent)
        }

    }
}