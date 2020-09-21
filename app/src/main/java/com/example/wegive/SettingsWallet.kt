package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.utils.FirebaseUtil
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.wallet_settings.*
import kotlinx.android.synthetic.main.wallet_settings.btn_back


private const val TAG = "SettingsWallet"
class SettingsWallet : AppCompatActivity() {
    private val firebaseObj: FirebaseUtil = FirebaseUtil()
    private var hasCreditCardInfo: Boolean = false
    private var last4Digits: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_settings)

        //listenToUser()
        getDataOnce()

        tv_deletecard.setOnClickListener {
            removeCardInformation()
            showWhenDoesntHaveCC()
        }

        tv_addCredit.setOnClickListener {
            carddetailsLayout.visibility = View.VISIBLE
        }

        btn_addcard.setOnClickListener {
            if (allFieldsValid()){
                val last4: String = cardNumber.text.toString().takeLast(4)
                updateUserCCInfo(last4)
                tv_CreditNumberLastFour.text = last4
                cardNumber.text.clear()
                month.text.clear()
                et_year.text.clear()
                cvv.text.clear()
                showWhenHasCC()
            }
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, SettingsPage::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun updateUserCCInfo(last4: String) {
        last4Digits = last4

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
        if (cardNumber.text.isNullOrEmpty() || month.text.isNullOrEmpty() || et_year.text.isNullOrEmpty() || cvv.text.isNullOrEmpty()){
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_LONG).show()
            return false
        }

        if (cardNumber.text.toString().length < 4){
            Toast.makeText(this, "Credit Card must be at least 4 digits!", Toast.LENGTH_LONG).show()
            return false
        }
        if ((month.text.toString().toInt() > 12) || (1 > month.text.toString().toInt())){
            Toast.makeText(this, "Please enter a valid month!", Toast.LENGTH_LONG).show()
            return false
        }

        if (et_year.text.toString().length < 4){
            Toast.makeText(this, "Please enter a valid year!", Toast.LENGTH_LONG).show()
            return false
        }

        if (cvv.text.toString().length != 3){
            Toast.makeText(this, "Please enter a valid 3-digit cvv!", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }







    private fun showWhenDoesntHaveCC() {
        // if no credit card is present the following should be showing

        //add new card text views
        addcreditLayout.visibility = View.VISIBLE
        usercreditLayout.visibility = View.INVISIBLE
        carddetailsLayout.visibility = View.INVISIBLE
    }

    private fun showWhenHasCC() {
        // if the user already added a card the following should be showing

        addcreditLayout.visibility = View.INVISIBLE
        usercreditLayout.visibility = View.VISIBLE
        carddetailsLayout.visibility = View.INVISIBLE
        tv_CreditNumberLastFour.visibility = View.VISIBLE
        tv_CreditNumberLastFour.setText(last4Digits)


//        //delete card text view
//        tv_deletecard.visibility = View.VISIBLE
//        tv_deletecard.isEnabled = true




    }

    private fun listenToUser() {
        Log.d(TAG, "just entered listenToUser()")
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
                    }
                    } else {
                        Log.d(TAG, "user DOES NOT have CC")
                        showWhenDoesntHaveCC()

                }
            }
        }
    }

    private fun getDataOnce(){
        Log.d(TAG, "just entered getDataOnce")
        firebaseObj.getUserRef().get().addOnSuccessListener { documentSnapshot ->
            Log.i(TAG, "Just entered addOnSuccessListener")
            val user=documentSnapshot.toObject(User::class.java)
            hasCreditCardInfo = user?.hasCC!!
            if (hasCreditCardInfo) {
                last4Digits = user.last4
                Log.d(TAG, "user DOES have CC")
                showWhenHasCC()
            } else {
                Log.d(TAG, "user DOES NOT have CC")
                showWhenDoesntHaveCC()
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