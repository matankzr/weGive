package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_wallet_page.*

private const val TAG = "WalletPage"

class WalletPage : AppCompatActivity() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_page)

        val tvTotalDonated = findViewById<TextView>(R.id.tv_totalDonated_walletPage)
        val tvAvailableCoins = findViewById<TextView>(R.id.tv_availableCoins_walletPage)

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        //add it only if it is not saved to database
        if (user != null) {
            userId = user.uid
        } else if (user == null) {
            Log.e(TAG, "User data is null")
        }


        val userRef = mFirebaseDatabaseInstance.collection("users").document(userId)
        val donationsReference = userRef.collection("donations")
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Document data: ${document.data}")
                    tvTotalDonated.text = document.get("totalAmountGiven").toString()
                    tvAvailableCoins.text = document.get("myCoins").toString()
                } else {
                    Log.d(TAG, "No such document!")
                }
            }
            .addOnFailureListener { exception -> Log.e(TAG, "Got failed with ", exception) }


        btn_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@WalletPage, MainPage::class.java)
                startActivity(intent);
            }
        })
    }
}
