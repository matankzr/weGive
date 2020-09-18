package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.R
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_donation.*
import kotlinx.android.synthetic.main.activity_store_paymaent.*
import kotlinx.android.synthetic.main.activity_wallet_page.*

private const val TAG="StorePayment"

class StorePayment: AppCompatActivity() {
    private var paymentAmount: Double = 0.0
    private var paymentString: String =""
    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_paymaent)
        Log.i(TAG, "Entered StorePayment onCreate")



        btn_pay.setOnClickListener {
            Log.i(TAG, "button pay pressed")
            pay()
        }
        btn_0.setOnClickListener {
            if(paymentString != "") {
                paymentString += "0"
                updateTextView()
            }
        }
        btn_1.setOnClickListener {
            paymentString += "1"
            updateTextView()
        }
        btn_2.setOnClickListener {
            paymentString += "2"
            updateTextView()
        }
        btn_3.setOnClickListener {
            paymentString += "3"
            updateTextView()
        }
        btn_4.setOnClickListener {
            paymentString += "4"
            updateTextView()
        }
        btn_5.setOnClickListener {
            paymentString += "5"
            updateTextView()
        }
        btn_6.setOnClickListener {
            paymentString += "6"
            updateTextView()
        }
        btn_7.setOnClickListener {
            paymentString += "7"
            updateTextView()
        }
        btn_8.setOnClickListener {
            paymentString += "8"
            updateTextView()
        }
        btn_9.setOnClickListener {
            paymentString += "9"
            updateTextView()
        }
        btn_dot.setOnClickListener {
            if(!paymentString.contains('.')) {
                paymentString += "."
                updateTextView()
            }
        }
        btn_delete.setOnClickListener {
            paymentString =  paymentString.substring(0,paymentString.length-1)
            updateTextView()
        }
    }

    private fun updateTextView(){
        textView17.setText(paymentString)
    }
    private fun pay() {
        if(paymentString[paymentString.length-1] == '.')
            paymentString += "00"
        paymentAmount = paymentString.toDouble()
        var myCoins: Long = 0
        firebaseObj.getUserRef().get().addOnSuccessListener { document ->
            if (document != null) {
                myCoins = document.get("myCoins") as Long
                if (paymentAmount <= myCoins) {
                    Log.d(TAG, "Payment successful! Payment amount is: $paymentAmount my coins are: $myCoins")
                    val docRef: DocumentReference = firebaseObj.getUserRef()
                    docRef?.update("myCoins", FieldValue.increment(-paymentAmount))
                } else {
                    Toast.makeText(
                        this,
                        "Payment failed! \n Payment amount is: $paymentAmount, WeGiveCoins: $myCoins",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


}
