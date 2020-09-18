package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.wegive.SettingsWallet
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_settings_page.*

private const val TAG = "SettingsPage"
class SettingsPage : AppCompatActivity() {


    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        btn_back.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsPage, MainPage::class.java)
                startActivity(intent);
            }
        })

        btn_logout.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                Log.i(TAG, "User wants to log out")
                //FirebaseAuth.getInstance().signOut()
                firebaseObj.getAuth().signOut()

                val intent = Intent(this@SettingsPage, LoginScreen::class.java)
                startActivity(intent);
            }
        })

        btn_account.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsPage, SettingsAccount::class.java)
                startActivity(intent);
            }
        })

        btn_privacy.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsPage, SettingsPrivacy::class.java)
                startActivity(intent);
            }
        })

        btn_terms.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsPage, SettingsTerms::class.java)
                startActivity(intent);
            }
        })

        btn_wallet.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@SettingsPage, SettingsWallet::class.java)
                startActivity(intent);
            }
        })
    }
}
