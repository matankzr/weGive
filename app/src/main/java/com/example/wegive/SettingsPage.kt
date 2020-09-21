package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.wegive.utils.FirebaseUtil
import kotlinx.android.synthetic.main.activity_settings_page.*

private const val TAG = "SettingsPage"
class SettingsPage : AppCompatActivity() {


    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        btn_back.setOnClickListener {
            val intent = Intent(this@SettingsPage, MainPage::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            Log.i(TAG, "User wants to log out")
            //FirebaseAuth.getInstance().signOut()
            firebaseObj.getAuth().signOut()

            val intent = Intent(this@SettingsPage, LoginScreen::class.java)
            startActivity(intent)
        }

        btn_account.setOnClickListener {
            val intent = Intent(this@SettingsPage, SettingsAccount::class.java)
            startActivity(intent)
        }

        btn_privacy.setOnClickListener {
            val intent = Intent(this@SettingsPage, SettingsPrivacy::class.java)
            startActivity(intent)
        }

        btn_terms.setOnClickListener {
            val intent = Intent(this@SettingsPage, SettingsTerms::class.java)
            startActivity(intent)
            finish()
        }

        btn_wallet.setOnClickListener {
            val intent = Intent(this@SettingsPage, SettingsWallet::class.java)
            startActivity(intent)
            finish()
        }

    }
}
