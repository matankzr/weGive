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

        tv_Credit.visibility = View.INVISIBLE;
        tv_CreditNumberLastFour.visibility = View.INVISIBLE;
        tv_deletecard.visibility = View.INVISIBLE;
    }
}