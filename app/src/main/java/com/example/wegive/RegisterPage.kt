package com.example.wegive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterPage : AppCompatActivity() {


    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var inputFirstName: EditText? = null
    private var inputLastName: EditText? = null
    private var btnImage: ImageView? = null
    private var btnSignUp: Button? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var useColu: Switch? = null
    private var btnback: ImageView? = null
    private var switchState: Boolean = false


    //private var auth : FirebaseAuth?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //auth = FirebaseAuth.getInstance()

        btnSignUp = findViewById(R.id.registerBtnRegisterPage) as Button
        inputEmail = findViewById(R.id.email) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        inputFirstName = findViewById(R.id.firstname) as EditText
        inputLastName = findViewById(R.id.lastname) as EditText
        btnImage = findViewById(R.id.img_picture) as ImageView
        useColu = findViewById(R.id.switch_colu) as Switch
        btnback = findViewById(R.id.btn_back) as ImageView

        btnback!!.setOnClickListener {
            val intent = Intent(this@RegisterPage, LoginScreen::class.java)
            startActivity(intent);
        }

        btnSignUp!!.setOnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()
            val firstName = inputFirstName!!.text.toString().trim()
            val lastName = inputLastName!!.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    applicationContext,
                    "Please enter your Email address",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    applicationContext,
                    "Please enter a valid Email address",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please enter your Password", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter mimimum 6 charcters",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(firstName)) {
                Toast.makeText(
                    applicationContext,
                    "Please enter your first name",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(lastName)) {
                Toast.makeText(applicationContext, "Please enter your last name", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (useColu!!.isChecked) {
                switchState = true
            }

            /*auth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, OnCompleteListener {
                        task ->
                    Toast.makeText(this@SingupActivity,"createUserWithEmail:onComplete"+task.isSuccessful,Toast.LENGTH_SHORT).show()
                    progressBar!!.setVisibility(View.VISIBLE)

                    if (!task.isSuccessful){
                        Toast.makeText(this@SingupActivity,"User Not crated",Toast.LENGTH_SHORT).show()
                        return@OnCompleteListener
                    }else{
                        val intent = Intent(this@RegisterPage, MainPage::class.java)
                        startActivity(intent);
                        finish()
                    }


                })
                */


        }


    }
}