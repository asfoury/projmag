package com.sdp13epfl2021.projmag.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sdp13epfl2021.projmag.R


class QRCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_code)


        val imageView = findViewById<ImageView>(R.id.qrcode_image)
        val byteArray = intent.getByteArrayExtra("qrcode")
        byteArray?.let {
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imageView.setImageBitmap(bmp)
        }
    }
}