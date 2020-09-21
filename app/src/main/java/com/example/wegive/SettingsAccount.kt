package com.example.wegive

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wegive.utils.FirebaseUtil
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.activity_settings_page.btn_back
import java.io.IOException
import java.util.*

private const val TAG="SettingsAccount"

class SettingsAccount : AppCompatActivity() {
    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_settings)

        btn_back.setOnClickListener {
            val intent = Intent(this@SettingsAccount, SettingsPage::class.java)
            startActivity(intent)
            finish()
        }

        listenToUser()

        // Set a click listener for the button widget
        switch_colu.setOnClickListener{
            // Change the switch button checked state on button click
            firebaseObj.getUserRef().update("useForColu",switch_colu.isChecked)
                .addOnCompleteListener {
                   // getDataOnce()
                }
        }



        img_picture.setOnClickListener {
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
                uploadImage()
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

        return output
    }

    private fun uploadImage() {
        if(filePath != null){
            val ref = firebaseObj.getStorageRef().child("users/" + UUID.randomUUID().toString())
            val uploadTask = ref.putFile(filePath!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                }
            }.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUploadRecordToDb(uri: String){
        firebaseObj.getFirestoreInstance()
        val userdb = firebaseObj.getUserRef()


        userdb
            .update("profile_image_url", uri)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }

    }


    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { value, error ->

            if (error!= null || value == null){
                Log.e(TAG, "Exception when adding snapshotListener", error)
                return@addSnapshotListener
            }
            val user=value.toObject(User::class.java)

            tv_userName_accountSettings.text = user?.userName
            tv_firstName_accountSettings.text = user?.firstName
            tv_lastName_accountSettings.text = user?.lastName
            tv_email_accountSettings.text = user?.email
            if (user?.profile_image_url!!.isNotEmpty()){
                Glide.with(applicationContext).load(value.get("profile_image_url")).circleCrop().into(img_picture)
            }

            switch_colu.isChecked = user.useForColu == true
        }
    }

    private fun getDataOnce() {
        Log.i(TAG, "just entered SettingsAccount::getDataOnce")

        //getting the data onetime
        //val docRef=mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        firebaseObj.getUserRef().get().addOnSuccessListener { documentSnapshot ->
            Log.i(TAG, "Just entered addOnSuccessListener")
            val user=documentSnapshot.toObject(User::class.java)

            //Display newly updated name and email
            tv_userName_accountSettings.text = user?.userName
            tv_firstName_accountSettings.text = user?.firstName
            tv_lastName_accountSettings.text = user?.lastName
            tv_email_accountSettings.text = user?.email
            switch_colu.isChecked = user?.useForColu == true

    //            switch_colu.isChecked = (user?.useForColu!!)
        }
    }
}
