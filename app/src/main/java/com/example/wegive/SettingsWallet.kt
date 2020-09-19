package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.utils.FirebaseUtil
import kotlinx.android.synthetic.main.wallet_settings.*


private const val TAG = "SettingsWallet"
class SettingsWallet : AppCompatActivity() {
    private val firebaseObj: FirebaseUtil = FirebaseUtil()
    private var hasCreditCardInfo: Boolean = false
    private var last4Digits: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_settings)

        listenToUser()

        tv_deletecard.setOnClickListener {
            removeCardInformation();
            showWhenDoesntHaveCC()
            tv_deletecard.isEnabled = false;
            tv_deletecard.visibility = View.INVISIBLE
            tv_addCredit.isEnabled = true
            tv_addCredit.visibility = View.VISIBLE

            tv_Credit_stars.visibility = View.INVISIBLE
            tv_CreditNumberLastFour.visibility = View.INVISIBLE
            et_cardNumber.isEnabled = false
        }

        tv_addCredit.setOnClickListener {
            tv_EnterCredCardNumber.visibility = View.VISIBLE
            et_cardNumber.visibility = View.VISIBLE
            et_cardNumber.isEnabled = true
            btn_addcard.visibility = View.VISIBLE
            btn_addcard.isEnabled = true
        }

        btn_addcard.setOnClickListener {
            if (allFieldsValid()){
                val last4: String = et_cardNumber.text.toString().takeLast(4)
                updateUserCCInfo(last4)
                tv_CreditNumberLastFour.setText(last4)
                et_cardNumber.text.clear()
                showWhenHasCC()
            }
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, SettingsPage::class.java)
            startActivity(intent);
            finish()
        }


    }

    private fun updateUserCCInfo(last4: String) {
        firebaseObj.getUserRef().update("hasCC",true)
            .addOnSuccessListener {
                Log.d(TAG, "CC info updated to true")
            }
            .addOnFailureListener {
                Log.d(TAG, "CC update failed!")
            }

        firebaseObj.getUserRef().update("last4",last4)
            .addOnSuccessListener {
                Log.d(TAG, "CC info updated last4 digits")
            }
            .addOnFailureListener {
                Log.d(TAG, "last4 update failed!")
            }
    }

    private fun allFieldsValid(): Boolean {
        if (et_cardNumber.text.toString().length < 4){
            Toast.makeText(this, "Credit Card must be at least 4 digits!", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }







    private fun showWhenDoesntHaveCC() {
        // if no credit card is present the following should be showing

        //add new card text views
        tv_addCredit.visibility = View.VISIBLE
        tv_addCredit.isEnabled = true
        img_Credit.visibility = View.VISIBLE

        //the adding card part
        btn_addcard.visibility = View.INVISIBLE
        tv_EnterCredCardNumber.visibility = View.INVISIBLE
        et_cardNumber.visibility = View.INVISIBLE
        et_cardNumber.isEnabled = false

        //delete card text view
        tv_deletecard.isEnabled = false;
        tv_deletecard.visibility = View.INVISIBLE

        //the credit card number text views
        tv_Credit_stars.visibility = View.INVISIBLE
        tv_CreditNumberLastFour.visibility = View.INVISIBLE
    }

    private fun showWhenHasCC() {
        // if the user already added a card the following should be showing

        //add new card text views
        tv_addCredit.visibility = View.INVISIBLE
        tv_addCredit.isEnabled = false
        img_Credit.visibility = View.INVISIBLE

        //the adding card part
        btn_addcard.visibility = View.INVISIBLE
        tv_EnterCredCardNumber.visibility = View.INVISIBLE
        et_cardNumber.visibility = View.INVISIBLE

        //delete card text view
        tv_deletecard.visibility = View.VISIBLE
        tv_deletecard.isEnabled = true

        //the credit card number text views
        tv_Credit_stars.visibility = View.VISIBLE
        tv_CreditNumberLastFour.visibility = View.VISIBLE //should get the last four digits
        tv_CreditNumberLastFour.setText(last4Digits)
    }

    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { value, error ->
            if (error != null){
                Log.w(TAG, "Listen failed for user", error)
                return@addSnapshotListener
            }

            if (value != null){
                if (value.get("hasCC") != null) {
                    hasCreditCardInfo = value.get("hasCC") as Boolean
                    if (hasCreditCardInfo) {
                        last4Digits = value.get("last4").toString()
                        Log.d(TAG, "user DOES have CC")
                        showWhenHasCC()
                    } else {
                        Log.d(TAG, "user DOES NOT have CC")
                        showWhenDoesntHaveCC()
                    }
                }
            }
        }
    }

    private fun removeCardInformation() {
        firebaseObj.getUserRef().update("hasCC",false)
            .addOnSuccessListener {
                Log.d(TAG, "CC info updated to false")
            }
            .addOnFailureListener {
                Log.d(TAG, "CC removal update failed!")
            }

        firebaseObj.getUserRef().update("last4","")
            .addOnSuccessListener {
                Log.d(TAG, "last 4 erased")
            }
            .addOnFailureListener {
                Log.d(TAG, "CC last4 update failed!")
            }
    }

}