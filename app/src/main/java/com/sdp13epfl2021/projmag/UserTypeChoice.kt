package com.sdp13epfl2021.projmag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.activities.ProfilePageActivity

class UserTypeChoice : AppCompatActivity() {
    lateinit var ProfButton: RadioButton
    lateinit var StuButton: RadioButton

    companion object{
        var isProfessor: Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type2)
        ProfButton = findViewById(R.id.radioProfessorType)
        StuButton = findViewById(R.id.radioStudentType)
        val intent = Intent(this, ProfilePageActivity::class.java)
        ProfButton.setOnClickListener{
            isProfessor = true
            startActivity(intent)
        }
        StuButton.setOnClickListener{
            isProfessor = false
            startActivity(intent)
        }
    }
}