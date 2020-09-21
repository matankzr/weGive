package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_donation.*
import kotlin.math.roundToInt


private const val TAG = "DonationView"

class DonationView: AppCompatActivity() {


    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    //fields passed in from caller activity
    private lateinit var receiverId: String
    private lateinit var type: String

    ////Fields to get from fireStore
    private lateinit var userName: String
    var numIds: Int = 0
    var totalNumDonationsInSystem: Int = 0
    var totalNumOrgDonationsInSystem: Int = 0
    var totalNumPersonDonationsInSystem: Int = 0
    var donationAmount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)
        receiverId = intent.getStringExtra("receiverID")
        type = intent.getStringExtra("type")
        tv_receiver.setText(receiverId)

        firebaseObj.getUserRef().get().addOnSuccessListener { document->
            if (document != null){
                userName = document.get("userName").toString()
                tv_username.setText(userName)
                numIds = document.getLong("totalNumberOfDonations")?.toInt() ?: 0
                Log.d(TAG, "numIds is now: ${numIds}")
            } else{
                Log.d(TAG, "no such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "failed with ", exception)
            }

        firebaseObj.getDonationsRef().get().addOnSuccessListener { document->
            if (document!= null){
                totalNumDonationsInSystem = document.getLong("totalNumberOfDonations")?.toInt() ?:0
                totalNumOrgDonationsInSystem = document.getLong("totalNumberOfDonationsToOrganizations")?.toInt() ?:0
                totalNumPersonDonationsInSystem = document.getLong("totalNumberOfDonationsToPeople")?.toInt() ?:0
            } else{
                Log.d(TAG, "no such document")
            }
        }
            .addOnFailureListener {
                Log.d(TAG, "failed with ", it)
            }


        btn_donate.setOnClickListener {
            if (isDonationAmountLegal()){
                sendDonation()
            }
            else{
                Toast.makeText(this, "You can't donate nothing!", Toast.LENGTH_LONG).show()
            }
        }

        btn_cancel.setOnClickListener {
            Toast.makeText(this, "Donation cancelled", Toast.LENGTH_LONG).show()
            endActivity() }

        btn_5nis.setOnClickListener {
            donationAmount +=5
            et_donationAmount.setText(donationAmount.toString()) }
        btn_10nis.setOnClickListener {
            donationAmount +=10
            et_donationAmount.setText(donationAmount.toString()) }
        btn_15nis.setOnClickListener {
            donationAmount +=15
            et_donationAmount.setText(donationAmount.toString()) }
        btn_20nis.setOnClickListener {
            donationAmount +=20
            et_donationAmount.setText(donationAmount.toString()) }
    }

    private fun isDonationAmountLegal(): Boolean {
        val inputString = et_donationAmount.text.toString()

        if (inputString.isNotEmpty()){
            val numberValue = inputString.toInt()
            return numberValue>0
        }
        return false
    }

    private fun sendDonation() {

        val orderRefID: String
        if (type == "p")
            orderRefID = type+totalNumPersonDonationsInSystem.toString()
        else if (type == "o")
            orderRefID = type+totalNumOrgDonationsInSystem.toString()
        else{
            orderRefID = "u"+totalNumOrgDonationsInSystem.toString()
        }

        val recvAmount: Float = et_donationAmount.text.toString().toFloat()
        val memo: String = et_memo_donationView.text.toString()
        //val favorite: Boolean = checkBox_addToFavorites.isChecked

        val donation = hashMapOf(
            "receiver_id" to receiverId,
            "donation_amount" to recvAmount,
            "memo" to memo,
            "date_donation" to FieldValue.serverTimestamp()
        )

        firebaseObj.getUserRef().collection("donations").document(orderRefID).set(donation)
        updateUserDocument(firebaseObj.getUserRef(), recvAmount)
        updateDonationManager(userName, receiverId, recvAmount, FieldValue.serverTimestamp(), orderRefID)
        Toast.makeText(this, "Donate Sent to ${receiverId}", Toast.LENGTH_LONG).show()
        endActivity()
    }

    private fun endActivity() {
        val intent = Intent(this@DonationView, MainPage::class.java)
        startActivity(intent)
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
                    val addToMyCoins = amountToAdd*0.05
                    docRef.update("myCoins", addToMyCoins)
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


        val docRef: DocumentReference = firebaseObj.getDonationsRef()

        docRef.update("totalNumberOfDonations", FieldValue.increment(1))
        if (type == "p"){
            firebaseObj.getDonationsRef().collection("donationsToPeople").document(orderRefID).set(donation)
            docRef.update("totalNumberOfDonationsToPeople", FieldValue.increment(1))
        }

        else if (type == "o"){
            firebaseObj.getDonationsRef().collection("donationsToOrganizations").document(orderRefID).set(donation)
            docRef.update("totalNumberOfDonationsToOrganizations", FieldValue.increment(1))
        }


        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Document data: ${document.data}")
                    val oldAmountGiven = document.get("totalGivenToPeople").toString().toInt()
                    val amountToAdd = recvAmount.roundToInt()
                    docRef.update("totalGivenToPeople",oldAmountGiven+amountToAdd)
                } else {
                    Log.d(TAG, "No such document!")
                }
            }
            .addOnFailureListener { exception -> Log.e(TAG, "Got failed with ", exception) }
    }

}



