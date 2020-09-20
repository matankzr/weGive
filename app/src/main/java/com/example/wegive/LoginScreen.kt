package com.example.wegive

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.wegive.ForgotPassword
import com.example.wegive.RegisterPage
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_login.*
//import kotlinx.android.synthetic.main.activity_login.loginBtn
//import kotlinx.android.synthetic.main.activity_login.loginEmail
//import kotlinx.android.synthetic.main.activity_login.loginFacebook
//import kotlinx.android.synthetic.main.activity_login.loginPassword
//import kotlinx.android.synthetic.main.activity_login.registerBtn
import kotlinx.android.synthetic.main.activity_login_screen.*

private const val TAG = "LoginScreen"
class LoginScreen : AppCompatActivity() {

//    private var inputEmail: EditText? = null
//    private var inputPassword: EditText? = null
//    private var btnSignIn: Button? = null
//    private var btnSignUp: Button? = null
//    private var btnResetPassword: Button? = null
//    private var showPass: ImageView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

//        btnSignIn = findViewById(R.id.registerBtn) as Button
//        btnSignUp = findViewById(R.id.loginBtn) as Button
//        inputEmail = findViewById(R.id.loginEmail) as EditText
//        inputPassword = findViewById(R.id.loginPassword) as EditText
//        btnResetPassword = findViewById(R.id.forgotPassBtn) as Button
//        showPass = findViewById(R.id.showPasswordBtn) as ImageView

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            goMainPage()
        }

        showPasswordBtn.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                loginPassword.inputType = InputType.TYPE_CLASS_TEXT
            }
            if (event.action == MotionEvent.ACTION_UP) {
                loginPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            true
        })

        registerBtn!!.setOnClickListener {
            val intent = Intent(this@LoginScreen, RegisterPage::class.java)
            startActivity(intent);
            finish()
        }

        forgotPassBtn.setOnClickListener {
            val intent = Intent(this@LoginScreen, ForgotPassword::class.java)
            startActivity(intent);
        }

        loginFacebook.setOnClickListener{
            //do something
        }


        //the user pressed the log in button

        loginBtn.setOnClickListener {
            val email = loginEmail!!.text.toString().trim()
            val password = loginPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please enter your Email address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Please enter your Password",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            Log.i(TAG, "Tried loggin in with email: ${email} and password: ${password}")

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show()
                    goMainPage()
                finish()
                } else{
                    Log.i(TAG,"signInWithEmailAndPassword failed", task.exception)
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun goMainPage() {
        Log.i(TAG, "goMainPage called")
        val intent = Intent(this, MainPage::class.java)

        //intent.putExtra()
        startActivity(intent)

        //finish will make it so that clicking back exits the app.
        //Without finish, after logging in and clicking back, you go back to login screen
        finish()
    }
}
