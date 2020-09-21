package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wegive.models.DonationsHistoryAdapter
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_donation_history.*

private const val TAG = "DonationsHistory"
class DonationHistory : AppCompatActivity() {

    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    //create data source for donations
    private var donations: MutableList<Donation> = mutableListOf()
    private lateinit var adapter: DonationsHistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_history)

        listenToDonations()

        btn_back_donationsHistory.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun listenToDonations() {
        adapter = DonationsHistoryAdapter(this, donations)
        recyclerView_DonationsHistory.adapter = adapter
        recyclerView_DonationsHistory.layoutManager = LinearLayoutManager(this)

        firebaseObj.getUserDonationsRef().orderBy("date_donation", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            Log.i(TAG, "Inside donationsReference.addSnapshotListener")

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            val donationsList = snapshot.toObjects(Donation::class.java)
            donations.clear()
            donations.addAll(donationsList)
            adapter.notifyDataSetChanged()
            for (donation in donationsList){
                Log.i(TAG, "Donation: $donation")
            }
        }
    }
}