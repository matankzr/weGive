package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_settings_page.*
import kotlinx.android.synthetic.main.activity_settings_page.btn_back

private const val TAG="SettingsAccount"

class SettingsAccount : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_settings)
        Log.i(TAG, "just entered SettingsAccount::onCreate")

        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
        val user= FirebaseAuth.getInstance().currentUser

        if (user != null) {
            userId=user.uid
            Log.e(TAG,"User data is null")
        }

        btn_back.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsAccount, SettingsPage::class.java)
                startActivity(intent);
            }
        })

        getDataOnce()

    }

    private fun getDataOnce() {
        Log.i(TAG, "just entered SettingsAccount::getDataOnce")

        //getting the data onetime
        val docRef=mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        docRef?.get()?.addOnSuccessListener { documentSnapshot ->
            Log.i(TAG, "Just entered addOnSuccessListener")
            val user=documentSnapshot.toObject(User::class.java)

            //Log.e(TAG,"user data is: "+user?.firstName+", "+user?.email)

            //Display newly updated name and email

            tv_userName_accountSettings.setText(user?.userName)
            tv_firstName_accountSettings.setText(user?.firstName)
            tv_lastName_accountSettings.setText(user?.lastName)
            tv_email_accountSettings.setText(user?.email)
            //tv_weGiveCoins.setText(user?.myCoins.toString())
            //  tv_amount_profile.setText("Points: "+user?.coins)
            //txt_user.setText(user?.name+", "+user?.email)

            //Clear edit text
//            email.setText(user?.email)
//            username.setText(user?.name)
        }
    }
}
