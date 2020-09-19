package com.example.wegive.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wegive.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_charity.view.*

private const val TAG = "CharityAdapter"

//https://www.andreasjakl.com/recyclerview-kotlin-style-click-listener-android/
class CharityAdapter(
    val context: Context,
    val charities: List<Charity>,
    val imageClickListener: (Charity) -> Unit,
    val favButtonClickListener: (Charity) -> Unit
) :
    RecyclerView.Adapter<CharityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "Inside onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_charity, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = charities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "Inside onBindViewHolder")
        holder.bind(charities[position], position, imageClickListener, favButtonClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            charity: Charity,
            position: Int,
            clickListener: (Charity) -> Unit,
            clickListener2: (Charity) -> Unit
        ) {

            Log.d(TAG, "Inside bind")

            itemView.tv_name_itemCharity.text = charity.charityName
            Glide.with(context).load(charity.imageURL).into(itemView.iv_photo_itemCharity)
            itemView.iv_photo_itemCharity.setOnClickListener { clickListener(charity) }
            //itemView.btn_favorite_itemCharity.setOnClickListener { clickListener2(charity) }
        }

    }
}
