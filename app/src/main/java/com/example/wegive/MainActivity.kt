package com.example.wegive

import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, SplashView::class.java)
        startActivity(intent)
        finish()
    }

}
