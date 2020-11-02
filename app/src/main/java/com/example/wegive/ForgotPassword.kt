package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.LoginScreen
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_passwordforget.*
import kotlinx.android.synthetic.main.activity_passwordforget.btn_back
import kotlinx.android.synthetic.main.activity_settings_page.*


private const val TAG = "ForgotPassword"

class ForgotPassword: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passwordforget)
        Log.d(TAG, "onCreate of ForgotPassword")

        mAuth = FirebaseAuth.getInstance()
        tv_notfound.visibility = View.INVISIBLE

        //auth = FirebaseAuth.getInstance()

        btn_back.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent);
            finish()
        }

        btn_recover.setOnClickListener(View.OnClickListener {
            if (loginEmail2.text.isNullOrEmpty()){
                Toast.makeText(applicationContext,"Please enter your email ",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val email = loginEmail2.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please enter your email ",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"A password reset link has been sent to your email",Toast.LENGTH_LONG).show()
                        tv_notfound.visibility = View.INVISIBLE
                    } else {
                        // failed!
                        Toast.makeText(this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                        tv_notfound.visibility = View.VISIBLE
                    }
                }

        })



    }
}
