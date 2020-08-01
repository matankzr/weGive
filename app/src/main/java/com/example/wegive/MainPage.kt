package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*

private const val TAG="MainPage"
class MainPage : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val user= FirebaseAuth.getInstance().currentUser
        //add it only if it is not saved to database
        if (user != null) {
            userId=user.uid
            Log.e(TAG,"User data is null")
        }

        btn_settings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, SettingsPage::class.java)
                startActivity(intent);
            }
        })

        btn_wallet.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, WalletPage::class.java)
                startActivity(intent);
            }
        })

        getDataOnce()
        //tv_helloPerson.setText("Hello")
    }

    private fun getDataOnce() {
        //getting the data onetime
        val docRef=mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        docRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val user=documentSnapshot.toObject(User::class.java)

            Log.e(TAG,"user data is changed"+user?.firstName+", "+user?.email)

            //Display newly updated name and email

            tv_helloPerson.setText("Hello " + user?.firstName)
            tv_totalDonations.setText(user?.TotalDonations.toString())
            tv_weGiveCoins.setText(user?.myCoins.toString())
          //  tv_amount_profile.setText("Points: "+user?.coins)
            //txt_user.setText(user?.name+", "+user?.email)

            //Clear edit text
//            email.setText(user?.email)
//            username.setText(user?.name)
        }
    }
}