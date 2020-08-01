package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_view)
        var intent = Intent(this, LoginScreen::class.java)
        Handler().postDelayed(Runnable {
            startActivity(intent)
            finish()
        }, 3000)
    }
}
