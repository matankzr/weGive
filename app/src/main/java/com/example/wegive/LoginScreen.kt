package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

private const val TAG = "LoginScreen"
class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            goMainPage()
        }

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            loginBtn.isEnabled = false
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()
            Log.i(TAG, "Tried loggin in with email: ${email} and password: ${password}")
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Email/Password cannot be empty", Toast.LENGTH_LONG).show()
                loginBtn.isEnabled=true
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                loginBtn.isEnabled=true
                if (task.isSuccessful){
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show()
                    goMainPage()
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
