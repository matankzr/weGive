package com.example.wegive.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wegive.Donation
import com.example.wegive.R
import kotlinx.android.synthetic.main.full_donation_description.view.*

class DonationsHistoryAdapter(val context: Context, val donations: List<Donation>): RecyclerView.Adapter<DonationsHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.full_donation_description, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount() = donations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donations[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(donation: Donation) {
            itemView.tv_amount_fullDonationHistory.text = "Amount: " + donation.donationAmount.toString()
            itemView.tv_To_fullDonationHistory.text = "To: " + donation.receiverId
            itemView.tv_date_FullDonationHistory.text = "Date:" + donation.donationDate.toString()
            itemView.tv_memo_fullDonationHistory.text = "Memo:" + donation.memo
        }
    }
}