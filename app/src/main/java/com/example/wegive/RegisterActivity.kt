package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    private var mAuth: FirebaseAuth?=null
    private var mFirebaseFirestoreInstances: FirebaseFirestore?=null

    //Creating member variable for userId and emailAddress
    private var userId:String?=null
    private var emailAddress:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Get Firebase Instances
        mAuth = FirebaseAuth.getInstance()
        //Get instance of FirebaseDatabase
        mFirebaseFirestoreInstances = FirebaseFirestore.getInstance()

        tv_alreadyRegistered.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }


    }

    fun onRegisterClicked(view: View) {
        //Validation checking
        if (TextUtils.isEmpty(et_userName.text.toString())){
            Toast.makeText(applicationContext,"Enter Username!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(et_firstName.text.toString())){
            Toast.makeText(applicationContext,"Enter First name!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(et_lastName.text.toString())){
            Toast.makeText(applicationContext,"Enter Last name!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(et_email_registerActvity.text.toString())){
            Toast.makeText(applicationContext,"Enter email address!",Toast.LENGTH_LONG).show()
        }
        if(TextUtils.isEmpty(et_password_registerActvity.text.toString())){
            Toast.makeText(applicationContext,"Enter password!",Toast.LENGTH_LONG).show()
        }
        if(et_password_registerActvity.text.toString().length<6){
            Toast.makeText(applicationContext,"Password is too short",Toast.LENGTH_LONG).show()
        }
        //Making progressBar visible
        progressBar!!.visibility=View.VISIBLE

        //creating user
        mAuth!!.createUserWithEmailAndPassword(et_email_registerActvity.text.toString(),et_password_registerActvity.text.toString())
            .addOnCompleteListener(this){task ->
                Toast.makeText(this,"createUserWithEmail:onComplete"+task.isSuccessful,Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.GONE

                // When the sign-in is failed, a message to the user is displayed. If the sign-in is successful, auth state listener will get notified, and logic to handle the signed-in user can be handled in the listener.
                if(task.isSuccessful){

                    //Getting current user from FirebaseAuth
                    val user= FirebaseAuth.getInstance().currentUser

                    //add username, email to database
                    userId=user!!.uid
                    emailAddress=user.email


                    //Creating a new user
                    //val myUser=User(et_lastName.text.toString(),emailAddress!!)
                    //val myUser = User(emailAddress!!,et_userName.text.toString(), et_firstName.text.toString(), et_lastName.text.toString())
                    val myUser = createUser(emailAddress!!)


                    //Try writing to Firestore
                    mFirebaseFirestoreInstances?.collection("users")?.document(userId!!)?.set(myUser)

                    val docRef=mFirebaseFirestoreInstances?.collection("users")?.document(userId!!)
                    startActivity(Intent(this,LoginScreen::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Authentication Failed"+task.exception,Toast.LENGTH_SHORT).show()
                    Log.e("MyTag",task.exception.toString())
                }
            }
    }

    private fun createUser(emailAddress: String): Any {
        var user = User().apply {
            email = emailAddress
            userName = et_userName.text.toString()
            firstName = et_firstName.text.toString()
            lastName = et_lastName.text.toString()
        }
        return user
    }
}