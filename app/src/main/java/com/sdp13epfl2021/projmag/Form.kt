package com.sdp13epfl2021.projmag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Form : AppCompatActivity() {
    private fun initUi() {
        setContentView(R.layout.activity_form)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
    }

}