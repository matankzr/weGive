package com.example.wegive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wegive.R
import kotlinx.android.synthetic.main.wallet_settings.*

class SettingsWallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_settings)

        // if no credit card is present the following should be showing

        //add new card text views
        tv_addCredit.visibility = View.VISIBLE
        img_Credit.visibility = View.VISIBLE

        //the adding card part
        btn_addcard.visibility = View.INVISIBLE
        textView6.visibility = View.INVISIBLE
        cardNumber.visibility = View.INVISIBLE

        //delete card text view
        tv_deletecard.visibility = View.INVISIBLE

        //the credit card number text views
        tv_Credit.visibility = View.INVISIBLE
        tv_CreditNumberLastFour.visibility = View.INVISIBLE

        // if the user already added a card the following should be showing

        //add new card text views
        tv_addCredit.visibility = View.INVISIBLE
        img_Credit.visibility = View.INVISIBLE

        //the adding card part
        btn_addcard.visibility = View.INVISIBLE
        textView6.visibility = View.INVISIBLE
        cardNumber.visibility = View.INVISIBLE

        //delete card text view
        tv_deletecard.visibility = View.VISIBLE

        //the credit card number text views
        tv_Credit.visibility = View.VISIBLE
        tv_CreditNumberLastFour.visibility = View.VISIBLE //should get the last four digits

    }
}