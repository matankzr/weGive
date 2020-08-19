package com.example.wegive

import android.os.Bundle
import android.text.TextUtils
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
import java.lang.reflect.Field
import kotlin.math.roundToInt


private const val TAG = "DonationFormActivity"

class DonationFormActivity : AppCompatActivity() {

    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    private var userId: String? = null

    lateinit var etReceiverID: EditText
    lateinit var etReceiverAmount: EditText
    lateinit var etMemo: EditText
    lateinit var sendButton: Button
    lateinit var favorite: CheckBox


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

        btn_donate_donationForm.setOnClickListener {
            Log.i(TAG, "Donate Button Clicked!")

            if (areAllFieldsLegal()) {
                sendDonation()
            }
        }

    }

    private fun sendDonation() {
        Log.i(TAG, "sendDonation called!")

        val recvId: String = etReceiverID.text.toString()
        val recvAmount: Float = etReceiverAmount.text.toString().toFloat()
        var memo: String = etMemo.text.toString()
        val favorite: Boolean = favorite.isChecked

        if (etMemo.text.toString().isEmpty()) {
            memo = ""
        }
        Toast.makeText(this, "Donate Button clicked for User ${userId}", Toast.LENGTH_LONG).show()

        Log.i(TAG, "ReceiverID: ${recvId}, Amount: " + recvAmount + ", Memo: ${memo}")

        val docRef = mFirebaseDatabaseInstance?.collection("users")?.document(userId!!)

        val donation = hashMapOf(
            "receiver_id" to recvId,
            "donation_amount" to recvAmount,
            "memo" to memo,
            "date_donation" to FieldValue.serverTimestamp(),
            "favorite" to favorite
        )

        docRef?.collection("donations")?.add(donation)
        updateUserDocument(docRef, recvAmount);
        finish()

        //https://code.luasoftware.com/tutorials/google-cloud-firestore/firestore-partial-update/
    }

    private fun updateUserDocument(docRef: DocumentReference?, recvAmount: Float) {
        docRef?.update("totalDonations", FieldValue.increment(1))

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