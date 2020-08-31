package com.example.wegive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listviewrow_donations.view.*


class DonationAdapter (val context: Context, val donations: List<Donation>):
    RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView ) {
        fun bind(donation: Donation) {
            itemView.tv_amount_donationItem.text = donation.donationAmount.toString()
            itemView.tv_date_donationItem.text = donation.donationDate.toString()
            itemView.tv_forWho_donationItem.text = donation.receiverId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listviewrow_donations, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = donations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(donations[position])
    }
}
////https://www.youtube.com/watch?v=afl_i6uvvU0

//https://www.youtube.com/watch?v=dxBhn0j8kws&t=1s



//class DonationAdapter(val mContext: Context,val layoutResId: Int, val donationList: List<Donation>): ArrayAdapter<Donation>(mContext, layoutResId, donationList){
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(mContext);
//        val view: View = layoutInflater.inflate(layoutResId, null);
//
//        val tvDonationFor = view.findViewById<TextView>(R.id.tv_forWho_donationItem)
//        val tvDonationAmount = view.findViewById<TextView>(R.id.tv_amount_donationItem)
//        val tvDonationDate = view.findViewById<TextView>(R.id.tv_date_donationItem)
//
//        val donation = donationList[position]
//        tvDonationFor.text = donation.receiverId
//        tvDonationAmount.text = donation.donationAmount.toString()
//        tvDonationDate.text = donation.donationDate.toString()
//        return view;
//
//    }
//}

//class DonationAdapter(val context: Context, val donations: List<Donation>) : RecyclerView.Adapter<DonationAdapter.ItemDonationViewHolder>() {
//    //"Don't need to understand what this line does!"
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDonationViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.listviewrow_donations, parent, false)
//        return ItemDonationViewHolder(view)
//
//    }
//
//    override fun getItemCount() = donations.size
//
//    override fun onBindViewHolder(holder: ItemDonationViewHolder, position: Int) {
//        TODO("Not yet implemented")
//        holder.bind(donations[position])
//    }
//
//    //ViewHolder represents a single row in recyle list
//    inner class ItemDonationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
////        val amountView: TextView = itemView.tv_amount_donationItem
////        val forView: TextView = itemView.tv_forWho_donationItem
////        val dateView: TextView = itemView.tv_date_donationItem
//
//        fun bind(donation: Donation){
//            //itemView.tv_amount_donationItem.text = donation?.donationAmount
//            itemView.tv_forWho_donationItem.text = donation.donationAmount.toString()
//            itemView.tv_date_donationItem.text = donation.donationDate.toString()
//        }
//
//    }
//}