package com.example.wegive.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wegive.R
import kotlinx.android.synthetic.main.item_charity.view.*

class CharityAdapter (val context: Context, val charities: List<Charity>) :
        RecyclerView.Adapter<CharityAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_charity, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = charities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(charities[position], position)
        holder.itemView.checkBox_favorite_itemCharity.setOnClickListener {

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var currentCharity: Charity?=null
        var currentPosition: Int = 0
        init{
            itemView.setOnClickListener {
                Toast.makeText(context, currentCharity!!.organizationName + " Clicked!", Toast.LENGTH_SHORT).show()
            }
        }
        fun bind(charity: Charity, position: Int){
            itemView.tv_name_itemCharity.text = charity.organizationName
            Glide.with(context).load(charity.imageURL).into(itemView.iv_photo_itemCharity)
            itemView.checkBox_favorite_itemCharity.isChecked = charity.favorite

            this.currentCharity = charity
            this.currentPosition =position
        }
    }

}