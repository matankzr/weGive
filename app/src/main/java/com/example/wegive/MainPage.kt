package com.example.wegive
import java.util.*

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*


private const val TAG="MainPage"
class MainPage : AppCompatActivity() {


    ////////
    //Links for recyclerView
    //https://www.youtube.com/watch?v=qva5ve_A8Co
    //https://www.youtube.com/watch?v=vpObpZ5MYSE


    /////////////////////

    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    //create data source for donations
    private var donations: MutableList<Donation> = mutableListOf()
    private lateinit var adapter: DonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        Log.i(TAG, "Inside MainPage onCreate")
        Log.d(TAG,"firebaseObj.getUserId: ${firebaseObj.getUserID()}")


        listenToUser()
        listenToDonations()


        btn_settings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, SettingsPage::class.java)
                startActivity(intent);
            }
        })

//        btn_dumbDonate_activityMainPage.setOnClickListener {
//            val intent = Intent(this, DonationFormActivity::class.java)
//            startActivity(intent)
//        }

        btn_wallet.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, WalletPage::class.java)
                startActivity(intent);
            }
        })

        btn_scan.setOnClickListener {
            val intent = Intent(this , ScanActivity::class.java)
            startActivity(intent);
        }
    }


    private fun listenToDonations() {
        adapter = DonationAdapter(this, donations)
        recyclerView_MainPage.adapter = adapter
        recyclerView_MainPage.layoutManager = LinearLayoutManager(this)

        firebaseObj.getUserDonationsRef().addSnapshotListener { snapshot, exception ->
            Log.i(TAG, "Inside donationsReference.addSnapshotListener")

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }


            if (snapshot != null) {
                val donationsList = snapshot.toObjects(Donation::class.java)
                donations.clear()
                donations.addAll(donationsList)
                adapter.notifyDataSetChanged()
                for (donation in donationsList){
                    Log.i(TAG, "Donation: ${donation}")
                }
            }
        }
    }

    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { snapshot, e ->
            //if there's an exception, skip
            if (e != null){
                Log.w(TAG, "Listen failed for user", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                tv_helloPerson.setText("Hello " + snapshot.get("firstName").toString())
                tv_totalNumberOfDonations.setText(snapshot.get("totalAmountGiven").toString())
                tv_weGiveCoins.setText(snapshot.get("myCoins").toString())
            }
        }
    }

}