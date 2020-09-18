package com.example.wegive

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wegive.models.*
import com.example.wegive.utils.FirebaseUtil
import kotlinx.android.synthetic.main.activity_store_paymaent.*
import kotlinx.android.synthetic.main.activity_wallet_page.*
import kotlinx.android.synthetic.main.activity_wallet_page.btn_back
import kotlinx.android.synthetic.main.item_charity.*


private const val TAG = "WalletPage"

class WalletPage : AppCompatActivity() {
    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    private var donations: MutableList<Donation> = mutableListOf()
    private var charityOrganizations: MutableList<Charity> = mutableListOf()
    private var stores: MutableList<Store> = mutableListOf()


    private var btnSelected: Int = 1

    private lateinit var adapter: DonationAdapter
    private lateinit var organizationAdapter: CharityAdapter
    private lateinit var storesAdapter: StoreAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_page)
        Log.i(TAG, "Entered onCreate")



        listenToUser()
        listenToDonations()
        listenToOrganizations()
        listenToStores()

        searchtxt.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "Search changed to: " + s.toString())
                if (s.toString().isNotEmpty()){
                    if (btnSelected == 1){
                        Log.d(TAG, "ButtonSelected = 1")
                        searchStores(s.toString().toLowerCase())
                    } else if (btnSelected == 2){
                        Log.d(TAG, "ButtonSelected = 2")
                        searchOrganizations(s.toString().toLowerCase())
                    }
                }

                if (s.toString().isEmpty()){
                    if (btnSelected == 1){
                        Log.d(TAG, "ButtonSelected = 1 on Empty Search")
                        listenToStores()
                    } else if (btnSelected == 2){
                        Log.d(TAG, "ButtonSelected = 2 on Empty Search")
                        listenToOrganizations()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })


        btn_organizations.setOnClickListener {
            Log.i(TAG, "button cat two selected")
            btnSelected = 2
            if (searchtxt.text.toString().isNotEmpty()){
                searchOrganizations(searchtxt.text.toString().toLowerCase())
            } else{
                listenToOrganizations()
            }
        }

        btn_stores.setOnClickListener {
            Log.i(TAG, "button cat one selected")
            btnSelected = 1
            if (searchtxt.text.toString().isNotEmpty()){
                searchStores(searchtxt.text.toString().toLowerCase())
            } else{
                listenToStores()
            }
        }


        btn_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@WalletPage, MainPage::class.java)
                startActivity(intent);
            }
        })
    }

    private fun searchOrganizations(keyword: String) {
        firebaseObj.getOrganizationsRef().whereArrayContains("keywords", keyword).addSnapshotListener { snapshot, exception ->
            Log.i(TAG,"inside charitiesRef.addSnapshotListener")

            organizationAdapter = CharityAdapter(this,
                charityOrganizations,
                { charity : Charity -> partItemClicked(charity) },
                { charity : Charity -> favButtonClickHandler(charity) })

            recyclerView_WalletPage.adapter = organizationAdapter
            recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val organizationsList = snapshot.toObjects(Charity::class.java)
                charityOrganizations.clear()
                charityOrganizations.addAll(organizationsList)
                organizationAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun searchStores(keyword: String) {
        firebaseObj.getStoresRef().whereArrayContains("keywords",keyword).addSnapshotListener { snapshot, exception ->
            Log.i(TAG,"inside charitiesRef.addSnapshotListener")

            storesAdapter = StoreAdapter(this, stores,{ store : Store -> storeClicked(store) })
            recyclerView_WalletPage.adapter = storesAdapter
            recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {

                var storeList = snapshot.toObjects(Store::class.java)
                stores.clear()
                stores.addAll(storeList)
                storesAdapter.notifyDataSetChanged()
            }
        }
    }

//    private fun getListOfStores(): MutableList<Store> {
//        var res: MutableList<Store> = mutableListOf()
//        Log.d(TAG, "inside getListOfStores()")
//        firebaseObj.getStoresRef().addSnapshotListener { snapshot, exception ->
//            Log.i(TAG,"inside charitiesRef.addSnapshotListener")
//
//            if (exception!= null || snapshot == null){
//                Log.e(TAG, "Exception when querying donations", exception)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null) {
//                val storeList = snapshot.toObjects(Store::class.java)
//                Log.d(TAG, "storeList (snapshot.toObjects) returned: $")
//                res = storeList
//            }
//        }
//
//        for (store in res){
//               Log.d(TAG, "store: ${store}")
//        }
//        return res
//    }

    private fun partItemClicked(charity : Charity) {
        Toast.makeText(this, "Clicked: ${charity.charityName}", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DonationView::class.java)
        intent.putExtra("receiverID",charity.charityName)
        intent.putExtra("type","o")
        startActivity(intent)

    }

    private fun favButtonClickHandler(charity : Charity) {
        var isFavorite: Boolean = btn_favorite_itemCharity.isChecked

        if (isFavorite){
            Toast.makeText(this, "Organization: ${charity.charityName} IS favorite!", Toast.LENGTH_SHORT).show()

        } else{
            Toast.makeText(this, "Organization: ${charity.charityName} IS NOT favorite ):", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listenToOrganizations() {
        firebaseObj.getOrganizationsRef().addSnapshotListener { snapshot, exception ->
            Log.i(TAG,"inside charitiesRef.addSnapshotListener")

            organizationAdapter = CharityAdapter(this,
                charityOrganizations,
                { charity : Charity -> partItemClicked(charity) },
                { charity : Charity -> favButtonClickHandler(charity) })

            recyclerView_WalletPage.adapter = organizationAdapter
            recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val organizationsList = snapshot.toObjects(Charity::class.java)
                charityOrganizations.clear()
                charityOrganizations.addAll(organizationsList)
                organizationAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun storeClicked(store: Store) {
        Toast.makeText(this, "Clicked: ${store.storeName} !!!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@WalletPage, StorePayment::class.java)
        intent.putExtra("storeName",store.storeName)
        intent.putExtra("storeAddress",store.storeWebsite)
        intent.putExtra("storeImageURL",store.imageURL)

        startActivity(intent)
    }



    private fun listenToStores() {


        firebaseObj.getStoresRef().addSnapshotListener { snapshot, exception ->
            Log.i(TAG,"inside charitiesRef.addSnapshotListener")

            storesAdapter = StoreAdapter(this, stores,{ store : Store -> storeClicked(store) })
            recyclerView_WalletPage.adapter = storesAdapter
            recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {

                var storeList = snapshot.toObjects(Store::class.java)
                stores.clear()
                stores.addAll(storeList)
                storesAdapter.notifyDataSetChanged()
            }
        }
    }




    private fun listenToDonations() {
        Log.i(TAG, "called listenToDonations")


        // Currently, Firestore doesn't allow searching for specific text. Would need to use 3rd party app
        //https://firebase.google.com/docs/firestore/solutions/search
        //var searchString: String = et_search_walletPage.text.toString()
        //Log.d(TAG,"searchString: ${searchString}")

        adapter = DonationAdapter(this, donations)
        recyclerView_WalletPage.adapter = adapter
        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)

        firebaseObj.getUserDonationsRef().addSnapshotListener { snapshot, exception ->
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
            }
        }
    }


//
//    private fun listenToTab2() {
//        Log.i(TAG, "called listenToTab2")
//        val donationsReference = firebaseObj.getUserDonationsRef().whereEqualTo("favorite", true)
//
//        adapter = DonationAdapter(this, donations)
//        recyclerView_WalletPage.adapter = adapter
//        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)
//
//        donationsReference.addSnapshotListener { snapshot, exception ->
//            Log.i(TAG, "Inside donationsReference.addSnapshotListener")
//
//            if (exception!= null || snapshot == null){
//                Log.e(TAG, "Exception when querying donations", exception)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null) {
//                val donationsList = snapshot.toObjects(Donation::class.java)
//                donations.clear()
//                donations.addAll(donationsList)
//                adapter.notifyDataSetChanged()
////                for (donation in donationsList){
////                    Log.i(TAG, "Donation: ${donation}")
////                }
//            }
//        }
//    }

    private fun listenToFavorites() {
        Log.i(TAG, "called listenToFavorites")
        Toast.makeText(this, "Clicked on favorites button!", Toast.LENGTH_SHORT).show()

//        favAdapter = FavoritesAdapter(this, favorites)
//        recyclerView_WalletPage.adapter = favAdapter
//        recyclerView_WalletPage.layoutManager = LinearLayoutManager(this)
//
//        firebaseObj.getUserFavoritesRef().addSnapshotListener { snapshot, exception ->
//            Log.i(TAG, "Inside donationsReference.addSnapshotListener")
//
//            if (exception!= null || snapshot == null){
//                Log.e(TAG, "Exception when querying favorites", exception)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null) {
//                val favoritesList = snapshot.toObjects(Favorite::class.java)
//                favorites.clear()
//                favorites.addAll(favoritesList)
//                favAdapter.notifyDataSetChanged()
//            }
//        }
    }

    private fun listenToUser() {
        Log.i(TAG, "called listenToUser")
        firebaseObj.getUserRef().addSnapshotListener { snapshot, e ->
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
