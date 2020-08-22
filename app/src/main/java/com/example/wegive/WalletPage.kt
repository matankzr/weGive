package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_wallet_page.*

private const val TAG = "WalletPage"

class WalletPage : AppCompatActivity() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private lateinit var userId: String
    private lateinit var donations: MutableList<Donation>
    private lateinit var adapter: DonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_page)

//        val tvTotalDonated = findViewById<TextView>(R.id.tv_totalDonated_walletPage)
//        val tvAvailableCoins = findViewById<TextView>(R.id.tv_availableCoins_walletPage)

        donations = mutableListOf()

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        //add it only if it is not saved to database
        if (user != null) {
            userId = user.uid
        } else if (user == null) {
            Log.e(TAG, "User data is null")
        }

        userRef = mFirebaseDatabaseInstance.collection("users").document(userId)

        listenToUser()
        listenToDonations()


        btn_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@WalletPage, MainPage::class.java)
                startActivity(intent);
            }
        })
    }

    private fun listenToDonations() {
        Log.i(TAG, "called listenToDonations")
        val donationsReference = userRef.collection("donations")

        adapter = DonationAdapter(this, donations)
        recyclerView_WalletPage.adapter = adapter
        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

//        Log.i(TAG, "Found donationsReference: ${donationsReference}")

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

    private fun listenToUser() {
        Log.i(TAG, "called listenToUser")
        userRef.addSnapshotListener { snapshot, e ->
            //if there's an exception, skip
            if (e != null){
                Log.w(TAG, "Listen failed for user", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                tv_totalDonated_walletPage.setText(snapshot.get("totalAmountGiven").toString() )
                tv_availableCoins_walletPage.setText(snapshot.get("myCoins").toString())
            }
        }
    }


}
