package com.example.wegive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private var userId:String?=null
    //create data source for donations
    private lateinit var donations: MutableList<Donation>
    private lateinit var adapter: DonationAdapter

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

        listenToUser(userRef)
        listenToDonations(userRef)


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

        btn_scan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                try {
                    val intent = Intent("com.google.zxing.client.android.SCAN")
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0)
                } catch (e: Exception) {
                    val marketUri: Uri = Uri.parse("market://details?id=com.google.zxing.client.android")
                    val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                    startActivity(marketIntent)
                }
            }
        })
    }

    private fun listenToDonations(userRef: DocumentReference) {
        val donationsReference = userRef.collection("donations")

        adapter = DonationAdapter(this, donations)
        recyclerView_MainPage.adapter = adapter
        recyclerView_MainPage.layoutManager = LinearLayoutManager(this)

        Log.i(TAG, "Found donationsReference: ${donationsReference}")

        donationsReference.addSnapshotListener { snapshot, exception ->
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

    private fun listenToUser(userRef: DocumentReference) {
        userRef.addSnapshotListener { snapshot, e ->
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