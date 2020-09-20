package com.example.wegive
import java.util.*

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.account_settings.*
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.item_store.view.*


private const val TAG="MainPage"
class MainPage : AppCompatActivity() {


    ////////
    //Links for recyclerView
    //https://www.youtube.com/watch?v=qva5ve_A8Co
    //https://www.youtube.com/watch?v=vpObpZ5MYSE


    /////////////////////

    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    //create data source for donations
    private var donations: MutableList<Donation> = mutableListOf()
    private lateinit var adapter: DonationAdapter
    private var canMakeDonations: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        Log.i(TAG, "Inside MainPage onCreate")
        Log.d(TAG,"firebaseObj.getUserId: ${firebaseObj.getUserID()}")


        listenToUser()
        listenToDonations()


        btn_settings.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, SettingsPage::class.java)
                startActivity(intent);
            }
        })

//        btn_dumbDonate_activityMainPage.setOnClickListener {
//            val intent = Intent(this, DonationFormActivity::class.java)
//            startActivity(intent)
//        }

        btn_wallet.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@MainPage, WalletPage::class.java)
                startActivity(intent);
            }
        })

        imageView5.setOnClickListener {
            val intent = Intent(this , DonationHistory::class.java)
            startActivity(intent);
            finish()
        }

        btn_scan.setOnClickListener {
            if (canMakeDonations){
                val intent = Intent(this , ScanActivity::class.java)
                startActivity(intent);
            } else{
                Toast.makeText(this,"Please enter credit card information in order to make a donation",Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun listenToDonations() {
        adapter = DonationAdapter(this, donations)
        recyclerView_MainPage.adapter = adapter
        recyclerView_MainPage.layoutManager = LinearLayoutManager(this)

        firebaseObj.getUserDonationsRef().limit(5).orderBy("date_donation",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
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

    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { snapshot, e ->
            //if there's an exception, skip
            if (e != null){
                Log.w(TAG, "Listen failed for user", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                tv_helloPerson.setText("Hello " + snapshot.get("userName").toString())
                tv_totalNumberOfDonations.setText(snapshot.get("totalAmountGiven").toString())
                tv_weGiveCoins.setText(snapshot.get("myCoins").toString())
                //val photo = getCroppedBitmap()
                var hasProfileImageURL = !snapshot.get("profile_image_url").toString().isNullOrEmpty()
                if (hasProfileImageURL){
                    Glide.with(getApplicationContext()).load(snapshot.get("profile_image_url")).circleCrop().into(userProfilePic_mainPage)
                } else{
                    userProfilePic_mainPage.setImageResource(R.drawable.profileplaceholder)
                }
                canMakeDonations = snapshot.get("hasCC") as Boolean
            }
        }
    }

    fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }

}