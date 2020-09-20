package com.example.wegive

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.wallet_settings.*

class SettingsWallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_settings)

        carddetailsLayout.visibility = View.INVISIBLE

        usercreditLayout.visibility = View.INVISIBLE
        addcreditLayout.visibility = View.VISIBLE

        tv_addCredit.setOnClickListener(View.OnClickListener {
            carddetailsLayout.visibility = View.VISIBLE
        })

    }

}