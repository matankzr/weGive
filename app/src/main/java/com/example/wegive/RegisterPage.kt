package com.example.wegive

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_back
import kotlinx.android.synthetic.main.activity_register.img_picture
import kotlinx.android.synthetic.main.activity_register.switch_colu
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


private const val TAG = "RegisterPage"
class RegisterPage : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    private var mFirebaseFirestoreInstances: FirebaseFirestore?=null

    //Creating member variable for userId and emailAddress
    private var userId:String?=null
    private var emailAddress:String?=null

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var useColu: Switch? = null
    private var btnback: ImageView? = null
    private var switchState: Boolean = false


    //private var auth : FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Get Firebase Instances
        mAuth = FirebaseAuth.getInstance()
        //Get instance of FirebaseDatabase
        mFirebaseFirestoreInstances = FirebaseFirestore.getInstance()


//        btnSignUp = findViewById(R.id.registerBtnRegisterPage) as Button
//        inputEmail = findViewById(R.id.email) as EditText
//        inputPassword = findViewById(R.id.password) as EditText
//        inputFirstName = findViewById(R.id.firstname) as EditText
//        inputLastName = findViewById(R.id.lastname) as EditText
//        btnImage = findViewById(R.id.img_picture) as ImageView
//        useColu = findViewById(R.id.switch_colu) as Switch
//        btnback = findViewById(R.id.btn_back) as ImageView
//        inputUserName = findViewById(R.id.username) as EditText

        btn_back.setOnClickListener {
            val intent = Intent(this@RegisterPage, LoginScreen::class.java)
            startActivity(intent);
        }

        registerBtnRegisterPage.setOnClickListener {
            onRegisterClicked()
        }
        img_picture.setOnClickListener{
            launchGallery()
        }


    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                img_picture.setImageBitmap(getCroppedBitmap(bitmap))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }


    fun onRegisterClicked(){
        Log.d(TAG, "entered onRegisterClicked")

        if (TextUtils.isEmpty(et_email_registerActvity.text.toString())) {
            Toast.makeText(
                applicationContext,
                "Please enter your Email address",
                Toast.LENGTH_LONG
            ).show()
        }
        Log.d(TAG, "email okay")

//        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email_registerActvity.text.toString()).matches()) {
//            Toast.makeText(
//                applicationContext,
//                "Please enter a valid Email address",
//                Toast.LENGTH_LONG
//            ).show()
//        }
        if (TextUtils.isEmpty(et_password_registerActvity.text.toString())) {
            Toast.makeText(applicationContext, "Please enter your Password", Toast.LENGTH_LONG)
                .show()
        }
        Log.d(TAG, "password okay")

        if (et_password_registerActvity.text.toString().length < 6) {
            Toast.makeText(
                applicationContext,
                "Password too short, enter mimimum 6 charcters",
                Toast.LENGTH_LONG
            ).show()
        }
        Log.d(TAG, "password length okay")

        if (TextUtils.isEmpty(et_firstName.text.toString())) {
            Toast.makeText(
                applicationContext,
                "Please enter your first name",
                Toast.LENGTH_LONG
            ).show()
        }
        Log.d(TAG, "first name okay")


        if (TextUtils.isEmpty(et_lastName.text.toString())) {
            Toast.makeText(applicationContext, "Please enter your last name", Toast.LENGTH_LONG)
                .show()
        }
        Log.d(TAG, "last name okay")

//        if (useColu!!.isChecked) {
//            switchState = true
//        }

        //creating user
        mAuth!!.createUserWithEmailAndPassword(
            et_email_registerActvity.text.toString(),
            et_password_registerActvity.text.toString()
        )
            .addOnCompleteListener(this){ task ->
                Toast.makeText(
                    this,
                    "createUserWithEmail:onComplete" + task.isSuccessful,
                    Toast.LENGTH_SHORT
                ).show()
                //progressBar.visibility= View.GONE

                // When the sign-in is failed, a message to the user is displayed. If the sign-in is successful, auth state listener will get notified, and logic to handle the signed-in user can be handled in the listener.
                if(task.isSuccessful){
                    Log.d(TAG, "inside task.isSuccessful")
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

                    uploadImage()


                    val docRef=mFirebaseFirestoreInstances?.collection("users")?.document(userId!!)
                    startActivity(Intent(this, LoginScreen::class.java))
                    finish()
                }else{
                    Toast.makeText(
                        this,
                        "Authentication Failed" + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MyTag", task.exception.toString())
                }
            }
    }

    private fun createUser(emailAddress: String): Any {
        var user = User().apply {
            email = et_email_registerActvity.text.toString()
            userName = et_userName.text.toString()
            firstName = et_firstName.text.toString()
            lastName = et_lastName.text.toString()
            useForColu = switch_colu.isChecked
        }
        return user
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref =
                FirebaseStorage.getInstance().getReference("users/" + UUID.randomUUID().toString())


            ref?.putFile(filePath!!).addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File location: $it")
                    addUploadRecordToDb(it.toString())
                }
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val userdb = mFirebaseFirestoreInstances?.collection("users")?.document(userId!!)
        //val data = HashMap<String, Any>()
        val data = uri

        if (userdb != null) {
            userdb
                .update("profile_image_url",data)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
                }
        }
    }
}