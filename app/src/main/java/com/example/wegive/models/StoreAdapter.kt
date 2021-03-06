package com.example.wegive.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wegive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_charity.view.*
import kotlinx.android.synthetic.main.item_store.view.*

class StoreAdapter(val context: Context, val stores: List<Store>,val clickListener: (Store)->Unit): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount() = stores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stores[position], clickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(store: Store, clickListener: (Store)->Unit) {
            itemView.tv_name_itemStore.text = store.storeName
            Glide.with(context).load(store.imageURL).into(itemView.iv_image_itemStore)
            //itemView.btn_favorite_itemStore.isChecked = store.favorite

            itemView.iv_image_itemStore.setOnClickListener { clickListener(store) }

        }
    }
}