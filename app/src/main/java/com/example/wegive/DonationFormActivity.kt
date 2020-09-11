package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_donation_form.*
import kotlin.math.roundToInt


private const val TAG = "DonationFormActivity"

class DonationFormActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private lateinit var userId: String
    private lateinit var userName: String


    private lateinit var donationManagerRef: DocumentReference


    lateinit var etReceiverID: EditText
    lateinit var etReceiverAmount: EditText
    lateinit var etMemo: EditText
    lateinit var sendButton: Button
    lateinit var favorite: CheckBox
    var numIds: Int = 0
    var totalNumDonationsInSystem: Int = 0
    var totalNumOrgDonationsInSystem: Int = 0
    var totalNumPersonDonationsInSystem: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_form)

        Log.i(TAG, "onCreate DonationFormActivity called")

        // get reference to button
        etReceiverID = findViewById(R.id.et_recipient_donationForm)
        etReceiverAmount = findViewById(R.id.et_amount_donationForm)
        etMemo = findViewById(R.id.et_memo_donationForm)
        sendButton = findViewById(R.id.btn_donate_donationForm)
        favorite = findViewById(R.id.checkBox_favorite_donationForm)

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userId = user.uid
        }


       userRef = mFirebaseDatabaseInstance.collection("users").document(userId)

        userRef.get().addOnSuccessListener {
            userName = it.get("userName").toString()
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


        btn_donate_donationForm.setOnClickListener {
            Log.i(TAG, "Donate Button Clicked!")

            if (areAllFieldsLegal()) {
                sendDonation()
            }
        }

    }

    private fun sendDonation() {
        Log.i(TAG, "sendDonation called!")

        val orderRefID: String = "p"+totalNumPersonDonationsInSystem.toString()

        val recvId: String = etReceiverID.text.toString()
        val recvAmount: Float = etReceiverAmount.text.toString().toFloat()
        var memo: String = etMemo.text.toString()
        val favorite: Boolean = favorite.isChecked



        if (etMemo.text.toString().isEmpty()) {
            memo = ""
        }
        Toast.makeText(this, "Donate Button clicked for User ${userId}", Toast.LENGTH_LONG).show()

        Log.i(TAG, "ReceiverID: ${recvId}, Amount: " + recvAmount + ", Memo: ${memo}")

        val donation = hashMapOf(
            "receiver_id" to recvId,
            "donation_amount" to recvAmount,
            "memo" to memo,
            "date_donation" to FieldValue.serverTimestamp(),
            "favorite" to favorite
        )

//        if (favorite)
//            addReceiverToFavorites(recvId)

//        userRef.collection("donations").add(donation)
        userRef.collection("donations").document(orderRefID).set(donation)
        updateUserDocument(userRef, recvAmount);

        updateDonationManager(userName, recvId, recvAmount, FieldValue.serverTimestamp(), orderRefID)



        val intent = Intent(this@DonationFormActivity, MainPage::class.java)
        startActivity(intent);
        finish()

        //https://code.luasoftware.com/tutorials/google-cloud-firestore/firestore-partial-update/
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

    private fun addReceiverToFavorites(recvId: String) {
        Log.i(TAG, "called addReceiverToFavorites")

        val fav = hashMapOf(
            "cause" to recvId
        )

        val dbInsance = userRef.collection("favorites").document(recvId)

        dbInsance.get()
            .addOnSuccessListener {
                if (it.exists()){
                    Log.d(TAG, "already in favorites!")
                    userRef.collection("favorites").document(recvId).update("donationNumbersForCause",FieldValue.arrayUnion(numIds))
                } else{
                    Log.d(TAG, "not in favorites!")
                    userRef.collection("favorites").document(recvId).set(fav)
                    userRef.collection("favorites").document(recvId).update("donationNumbersForCause",FieldValue.arrayUnion(numIds))
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "checkIfRecvIdInFavorites encounted failure")
            }
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

    private fun areAllFieldsLegal(): Boolean {
        Log.i(TAG, "areAllFieldsLegal called!")
        var check1: Boolean = true
        var check2: Boolean = true
        var check3: Boolean = true


        val recvId: String = etReceiverID.text.toString()
        val recvAmount: Float? = etReceiverAmount.text.toString().toFloatOrNull()
        var memo: String = etMemo.toString()


        if (etMemo.text.toString().isEmpty()) {
            memo = ""
        }

        //To check values as expected:
//        Log.i(TAG, "Value of recvAmount: " + recvAmount)
//        Log.i(TAG, "Value of recvId: ${recvId}")
//        Log.i(TAG, "Value of memo: ${memo}")

        if (recvId.isEmpty()) {
            Log.i(TAG, "Error: Recipient ID cannot be empty!")
            Toast.makeText(applicationContext, "Recipient ID cannot be empty!", Toast.LENGTH_LONG)
                .show()
            check1 = false
        }

        if (recvAmount != null) {
            if (recvAmount < 1.0) {
                Log.i(TAG, "Error: amountVal is < 1")
                Toast.makeText(
                    applicationContext,
                    "Enter amount greater than 1!",
                    Toast.LENGTH_LONG
                ).show()
                check2 = false
            }
        }

        if (recvAmount == null) {
            Log.i(TAG, "Error: amount can't be empty!")
            Toast.makeText(applicationContext, "Enter amount!", Toast.LENGTH_LONG).show()
            check3 = false
        }

        var res: Boolean = check1 && check2 && check3
        Log.i(TAG, "areAllFieldsLegal returning: " + res)
        return (res)
    }
}