package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.activity_settings_page.btn_back

private const val TAG="SettingsAccount"

class SettingsAccount : AppCompatActivity() {
    private val firebaseObj: FirebaseUtil = FirebaseUtil()

//    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
//    private var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_settings)
        Log.i(TAG, "just entered SettingsAccount::onCreate")

        btn_back.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsAccount, SettingsPage::class.java)
                startActivity(intent);
            }
        })

        //getDataOnce()
        listenToUser()

        // Set a click listener for the button widget
        switch_colu.setOnClickListener{
            // Change the switch button checked state on button click
            firebaseObj.getUserRef().update("useForColu",switch_colu.isChecked)
                .addOnCompleteListener {
                   // getDataOnce()
                }
//            if(switch_colu.isChecked){
//                firebaseObj.getUserRef().update("useForColu",false)
//            } else{
//                firebaseObj.getUserRef().update("useForColu",true)
//            }

        }



    }

    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { value, error ->

            if (error!= null || value == null){
                Log.e(TAG, "Exception when adding snapshotListener", error)
                return@addSnapshotListener
            }
            if (value != null) {
                val user=value.toObject(User::class.java)

                tv_userName_accountSettings.setText(user?.userName)
                tv_firstName_accountSettings.setText(user?.firstName)
                tv_lastName_accountSettings.setText(user?.lastName)
                tv_email_accountSettings.setText(user?.email)
                //switch_colu.isChecked = (user?.useForColu!!)

                if (user?.useForColu == true){
                    switch_colu.isChecked = true
                }
                else{
                    switch_colu.isChecked = false
                }
            }
        }
    }

    private fun getDataOnce() {
        Log.i(TAG, "just entered SettingsAccount::getDataOnce")

        //getting the data onetime
        //val docRef=mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        firebaseObj.getUserRef()?.get()?.addOnSuccessListener { documentSnapshot ->
            Log.i(TAG, "Just entered addOnSuccessListener")
            val user=documentSnapshot.toObject(User::class.java)

            //Display newly updated name and email
            tv_userName_accountSettings.setText(user?.userName)
            tv_firstName_accountSettings.setText(user?.firstName)
            tv_lastName_accountSettings.setText(user?.lastName)
            tv_email_accountSettings.setText(user?.email)
            if (user?.useForColu == true){
                    switch_colu.isChecked = true
                }
                else{
                    switch_colu.isChecked = false
                }

//            switch_colu.isChecked = (user?.useForColu!!)
        }
    }
}
