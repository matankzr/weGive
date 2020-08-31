package com.example.wegive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wegive.models.Favorite
import com.example.wegive.models.FavoritesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_wallet_page.*

private const val TAG = "WalletPage"

class WalletPage : AppCompatActivity() {
    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    private lateinit var userRef: DocumentReference
    private lateinit var userId: String
    private lateinit var donations: MutableList<Donation>
    private lateinit var favorites: MutableList<Favorite>
    private lateinit var adapter: DonationAdapter
    private lateinit var favAdapter: FavoritesAdapter
    private var btnSelected: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_page)

        Log.i(TAG, "Entered onCreate")

        donations = mutableListOf()
        favorites = mutableListOf()

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        //add it only if it is not saved to database
        if (user != null) {
            userId = user.uid
        } else if (user == null) {
            Log.e(TAG, "User data is null")
        }

        userRef = mFirebaseDatabaseInstance.collection("users").document(userId)

        listenToUser()
        listenToDonations()

        btn_cat_two.setOnClickListener {
            Log.i(TAG, "button cat two selected")
            btnSelected = 2
            listenToTab2()
        }

        btn_cat_one.setOnClickListener {
            Log.i(TAG, "button cat one selected")
            btnSelected = 1
            listenToDonations()
        }

        btn_fav.setOnClickListener {
            Log.i(TAG, "button favorites selected")
            btnSelected = 3
            listenToFavorites()
        }

        btn_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@WalletPage, MainPage::class.java)
                startActivity(intent);
            }
        })
    }

    private fun listenToDonations() {
        Log.i(TAG, "called listenToDonations")

        // Currently, Firestore doesn't allow searching for specific text. Would need to use 3rd party app
        //https://firebase.google.com/docs/firestore/solutions/search
        //var searchString: String = et_search_walletPage.text.toString()
        //Log.d(TAG,"searchString: ${searchString}")


        val donationsReference = userRef.collection("donations")

        adapter = DonationAdapter(this, donations)
        recyclerView_WalletPage.adapter = adapter
        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

        donationsReference.addSnapshotListener { snapshot, exception ->
            Log.i(TAG, "Inside donationsReference.addSnapshotListener")

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val donationsList = snapshot.toObjects(Donation::class.java)
                donations.clear()
                donations.addAll(donationsList)
                adapter.notifyDataSetChanged()
                for (donation in donationsList){
                    Log.i(TAG, "Donation: ${donation}")
                }
            }
        }
    }

    private fun listenToTab2() {
        Log.i(TAG, "called listenToTab2")
        val donationsReference = userRef.collection("donations").whereEqualTo("favorite", true)

        adapter = DonationAdapter(this, donations)
        recyclerView_WalletPage.adapter = adapter
        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

//        Log.i(TAG, "Found donationsReference: ${donationsReference}")

        donationsReference.addSnapshotListener { snapshot, exception ->
            Log.i(TAG, "Inside donationsReference.addSnapshotListener")

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val donationsList = snapshot.toObjects(Donation::class.java)
                donations.clear()
                donations.addAll(donationsList)
                adapter.notifyDataSetChanged()
                for (donation in donationsList){
                    Log.i(TAG, "Donation: ${donation}")
                }
            }
        }
    }

    private fun listenToFavorites() {
        Log.i(TAG, "called listenToFavorites")
        val favoritesRef = userRef.collection("favorites")

        favAdapter = FavoritesAdapter(this, favorites)
        recyclerView_WalletPage.adapter = favAdapter
        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

        favoritesRef.addSnapshotListener { snapshot, exception ->
            Log.i(TAG, "Inside donationsReference.addSnapshotListener")

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying favorites", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val favoritesList = snapshot.toObjects(Favorite::class.java)
                favorites.clear()
                favorites.addAll(favoritesList)
                favAdapter.notifyDataSetChanged()
                for (favorite in favoritesList){
                    Log.i(TAG, "Favorite: ${favorite}")
                }
            }
        }
    }

    private fun listenToUser() {
        Log.i(TAG, "called listenToUser")
        userRef.addSnapshotListener { snapshot, e ->
            //if there's an exception, skip
            if (e != null){
                Log.w(TAG, "Listen failed for user", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                tv_totalDonated_walletPage.setText(snapshot.get("totalAmountGiven").toString() )
                tv_availableCoins_walletPage.setText(snapshot.get("myCoins").toString())
            }
        }
    }


}
