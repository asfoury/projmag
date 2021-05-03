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
        val savedTypeChoice :String = "userTypeChoicePrefs"
        val settings = getSharedPreferences(savedTypeChoice, 0)
        val editor = settings.edit()
        val professorButton: RadioButton = findViewById(R.id.radioProfessorType)
        val studentButton: RadioButton= findViewById(R.id.radioStudentType)
        val intent = Intent(this, ProfilePageActivity::class.java)
        professorButton.setOnClickListener{
            isProfessor = true
            editor.putBoolean("isProfessor",true)
            editor.commit()
            startActivity(intent)
        }
        studentButton.setOnClickListener{
            isProfessor = false
            editor.putBoolean("isProfessor",false)
            editor.commit()
            startActivity(intent)
        }
    }
}