package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.LoginScreen
import kotlinx.android.synthetic.main.activity_passwordforget.*
import kotlinx.android.synthetic.main.activity_passwordforget.btn_back
import kotlinx.android.synthetic.main.activity_settings_page.*

class ForgotPassword: AppCompatActivity() {


    private var email : EditText? = null
    private var btnresetPass:Button?=null
    private var btnback:ImageView?=null
    private var notfound : TextView? = null


    //private var auth:FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passwordforget)

        email = findViewById(R.id.loginEmail2) as EditText
        btnresetPass = findViewById(R.id.forgotPassBtn)as Button
        btnback = findViewById(R.id.btn_back) as ImageView
        notfound = findViewById(R.id.tv_notfound) as TextView

        notfound!!.visibility = View.INVISIBLE

        //auth = FirebaseAuth.getInstance()

        btn_back.setOnClickListener {
            val intent = Intent(this@ForgotPassword, LoginScreen::class.java)
            startActivity(intent);
        }

        btnresetPass!!.setOnClickListener(View.OnClickListener {
            val email = email!!.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please enter your email ",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            /*auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener(OnCompleteListener {
                        task ->
                    if (task.isSuccessful){ //found
                        Toast.makeText(this@ResetPasswordActivity,"We have to sent you instraction in your email",Toast.LENGTH_LONG).show()
                    }else{ //not found
                        notfound!!.visibility = View.VISIBLE
                    }
                    progressbar!!.setVisibility(View.GONE)
                })


             */
        })



    }
}
