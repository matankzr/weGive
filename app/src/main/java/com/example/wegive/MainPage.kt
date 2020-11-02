package com.example.wegive

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wegive.utils.FirebaseUtil
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main_page.*


private const val TAG="MainPage"
class MainPage : AppCompatActivity() {


    ////////
    //Links for recyclerView
    //https://www.youtube.com/watch?v=qva5ve_A8Co
    //https://www.youtube.com/watch?v=vpObpZ5MYSE


    /////////////////////
    private var position: Int = -1

    private val firebaseObj: FirebaseUtil = FirebaseUtil()

    //create data source for donations
    private var donations: MutableList<Donation> = mutableListOf()
    private lateinit var adapter: DonationAdapter
    private var canMakeDonations: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        Log.i(TAG, "Inside MainPage onCreate")
        Log.d(TAG, "firebaseObj.getUserId: ${firebaseObj.getUserID()}")
        val imageArray = arrayOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
        )


        listenToUser()
        listenToDonations()



        btn_settings.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainPage, SettingsPage::class.java)
                startActivity(intent)
            }
        })

//        btn_dumbDonate_activityMainPage.setOnClickListener {
//            val intent = Intent(this, DonationFormActivity::class.java)
//            startActivity(intent)
//        }

        btn_wallet.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@MainPage, WalletPage::class.java)
                startActivity(intent)
            }
        })

        imageView5.setOnClickListener {
            val intent = Intent(this, DonationHistory::class.java)
            startActivity(intent)
            finish()
        }

        btn_scan.setOnClickListener {
            if (canMakeDonations){
                val intent = Intent(this , ScanActivity::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this,"Please enter credit card information in order to make a donation",
                    Toast.LENGTH_LONG).show()
            }
        }
        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                    var z = 0
                    while (z < imageArray.size + 4) {
                        if (z < imageArray.size) {
                            sleep(3000)
                            runOnUiThread { imageView4.setImageResource(imageArray[z]) }
                        } else {
                            z = 0
                        }
                        z++
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        timer.start()
    }




    private fun listenToDonations() {
        adapter = DonationAdapter(this, donations)
        recyclerView_MainPage.adapter = adapter
        recyclerView_MainPage.layoutManager = LinearLayoutManager(this)

        firebaseObj.getUserDonationsRef().limit(5).orderBy(
            "date_donation",
            Query.Direction.DESCENDING
        ).addSnapshotListener { snapshot, exception ->
            if (exception!= null || snapshot == null){
                Log.e(TAG, "Exception when querying donations", exception)
                return@addSnapshotListener
            }


            val donationsList = snapshot.toObjects(Donation::class.java)
            donations.clear()
            donations.addAll(donationsList)
            adapter.notifyDataSetChanged()
            for (donation in donationsList){
                Log.i(TAG, "Donation: ${donation}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun listenToUser() {
        firebaseObj.getUserRef().addSnapshotListener { snapshot, e ->
            //if there's an exception, skip
            if (e != null){
                Log.w(TAG, "Listen failed for user", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                tv_helloPerson.text = "Hello " + snapshot.get("userName").toString()
                tv_totalNumberOfDonations.text = snapshot.get("totalAmountGiven").toString()
                tv_weGiveCoins.text = "%.2f".format(snapshot.get("myCoins"))
//                tv_weGiveCoins.text = snapshot.get("myCoins").toString()
                val hasProfileImageURL = snapshot.get("profile_image_url").toString().isNotEmpty()
                if (hasProfileImageURL){
                    Glide.with(applicationContext).load(snapshot.get("profile_image_url")).circleCrop().into(userProfilePic_mainPage)
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
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

}