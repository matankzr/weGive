package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wegive.model.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*




private const val TAG="MainPage"
class MainPage : AppCompatActivity() {


    ////////
    //Links for recyclerView
    //https://www.youtube.com/watch?v=qva5ve_A8Co
    //https://www.youtube.com/watch?v=vpObpZ5MYSE


    /////////////////////

    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private var userId:String?=null
    //create data source for donations
    private lateinit var donations: MutableList<Donation>
    private lateinit var myFireStoreObject: FirestoreUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        Log.i(TAG, "Inside MainPage onCreate")

        donations = mutableListOf()
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val user= FirebaseAuth.getInstance().currentUser

        if (user != null) {
            userId=user.uid
            Log.i(TAG, "Current user uid: ${userId}")
        }

        val userRef = mFirebaseDatabaseInstance.collection("users").document(userId!!)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Document data: ${document.data}")
                    tv_helloPerson.setText("Hello " + document.get("firstName").toString())
                    tv_totalDonations.setText(document.get("totalAmountGiven").toString())
                    tv_weGiveCoins.setText(document.get("myCoins").toString())
                } else {
                    Log.d(TAG, "No such document!")
                }
            }
            .addOnFailureListener { exception -> Log.e(TAG, "Got failed with ", exception) }

        val donationsReference = userRef.collection("donations")

//
//        Log.i(TAG, "Found donationsReference: ${donationsReference}")
//        donationsReference.addSnapshotListener { snapshot, exception ->
//            Log.i(TAG, "Inside donationsReference.addSnapshotListener")
//
//            if (exception!= null || snapshot == null){
//                Log.e(TAG, "Exception when querying donations", exception)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null) {
//                for (document in snapshot.documents){
//                    Log.i(TAG, "Donation: ${document.id}: ${document.data}")
//                }
//            }
//        }

        btn_settings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, SettingsPage::class.java)
                startActivity(intent);
            }
        })

        btn_dumbDonate_activityMainPage.setOnClickListener {
            val intent = Intent(this, DonationFormActivity::class.java)
            startActivity(intent)
        }

        btn_wallet.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, WalletPage::class.java)
                startActivity(intent);
            }
        })

       // getDataOnce()
    }

    private fun getDataOnce() {
        //getting the data onetime
        val docRef=mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        docRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val user=documentSnapshot.toObject(User::class.java)

//            Log.e(TAG,"user data is changed"+user?.firstName+", "+user?.email)

            //Display newly updated name and email
            tv_helloPerson.setText("Hello " + user?.firstName)
            tv_totalDonations.setText(user?.TotalDonations.toString())
            tv_weGiveCoins.setText(user?.myCoins.toString())
        }
    }
}