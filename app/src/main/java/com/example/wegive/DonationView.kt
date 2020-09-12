package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_donation.*
import kotlin.math.roundToInt


private const val TAG = "DonationView"

class DonationView: AppCompatActivity() {

    private lateinit var receiverId: String


    var numIds: Int = 0
    var totalNumDonationsInSystem: Int = 0
    var totalNumOrgDonationsInSystem: Int = 0
    var totalNumPersonDonationsInSystem: Int = 0
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var donationManagerRef: DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)
        receiverId = intent.getStringExtra("receiverID")

        tv_receiver_donationView.setText(receiverId)
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userId = user.uid
        }
        userRef = mFirebaseDatabaseInstance.collection("users").document(userId)
        userRef.get().addOnSuccessListener {
            userName = it.get("userName").toString()
        }
        userRef.get().addOnSuccessListener { document->
            if (document != null){
                numIds = document.getLong("totalNumberOfDonations")?.toInt() ?: 0
                Log.d(TAG, "numIds is now: ${numIds}")
            } else{
                Log.d(TAG, "no such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        donationManagerRef = mFirebaseDatabaseInstance.collection("donations").document("donationManager")
        donationManagerRef.get().addOnSuccessListener { document->
            if (document!= null){
                totalNumDonationsInSystem = document.getLong("totalNumberOfDonations")?.toInt() ?:0
                totalNumOrgDonationsInSystem = document.getLong("totalNumberOfDonationsToOrganizations")?.toInt() ?:0
                totalNumPersonDonationsInSystem = document.getLong("totalNumberOfDonationsToPeople")?.toInt() ?:0
            } else{
                Log.d(TAG, "no such document bitch")
            }
        }
            .addOnFailureListener {
                Log.d(TAG, "Bitch get failed with ", it)
            }


        btn_donate.setOnClickListener { sendDonation() }
        btn_cancel.setOnClickListener { endActivity() }
    }

    private fun sendDonation() {

        val orderRefID: String = "p"+totalNumPersonDonationsInSystem.toString()

        val recvAmount: Float = editTextNumber.text.toString().toFloat()
        var memo: String = et_memo_donationView.text.toString()
        val favorite: Boolean = checkBox_addToFavorites.isChecked

        val donation = hashMapOf(
            "receiver_id" to receiverId,
            "donation_amount" to recvAmount,
            "memo" to memo,
            "date_donation" to FieldValue.serverTimestamp(),
            "favorite" to favorite
        )

        userRef.collection("donations").document(orderRefID).set(donation)
        updateUserDocument(userRef, recvAmount);
        updateDonationManager(userName, receiverId, recvAmount, FieldValue.serverTimestamp(), orderRefID)
        Toast.makeText(this, "Donate Button clicked for User ${userId}", Toast.LENGTH_LONG).show()
        endActivity()
    }

    private fun endActivity() {
        val intent = Intent(this@DonationView, MainPage::class.java)
        startActivity(intent);
        finish()
    }

    private fun updateUserDocument(docRef: DocumentReference?, recvAmount: Float) {
        docRef?.update("totalNumberOfDonations", FieldValue.increment(1))

        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Document data: ${document.data}")
                    val oldAmountGiven = document.get("totalAmountGiven").toString().toInt()
                    val amountToAdd = recvAmount.roundToInt()
                    docRef.update("totalAmountGiven",oldAmountGiven+amountToAdd)
                } else {
                    Log.d(TAG, "No such document!")
                }
            }
            ?.addOnFailureListener { exception -> Log.e(TAG, "Got failed with ", exception) }

        //https://bezkoder.com/kotlin-convert-string-to-int-long-float-double/
    }

    private fun updateDonationManager(
        userName: String,
        recvId: String,
        recvAmount: Float,
        serverTimestamp: FieldValue,
        orderRefID: String
    ) {

        val donation = hashMapOf(
            "from" to userName,
            "to" to recvId,
            "donation_amount" to recvAmount,
            "date_donation" to serverTimestamp
        )

        donationManagerRef.collection("donationsToPeople").document(orderRefID).set(donation)

        val docRef: DocumentReference = donationManagerRef

        docRef?.update("totalNumberOfDonations", FieldValue.increment(1))
        docRef?.update("totalNumberOfDonationsToPeople", FieldValue.increment(1))

        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Document data: ${document.data}")
                    val oldAmountGiven = document.get("totalGivenToPeople").toString().toInt()
                    val amountToAdd = recvAmount.roundToInt()
                    docRef.update("totalGivenToPeople",oldAmountGiven+amountToAdd)
                } else {
                    Log.d(TAG, "No such document!")
                }
            }
            ?.addOnFailureListener { exception -> Log.e(TAG, "Got failed with ", exception) }
    }

}
