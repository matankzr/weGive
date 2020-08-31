package com.example.wegive.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wegive.models.Favorite
import com.example.wegive.R
import kotlinx.android.synthetic.main.row_favorite_cause.view.*

class FavoritesAdapter(val context: Context, val favorites: List<Favorite>):
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView ) {
        fun bind(fav: Favorite) {
            itemView.tv_amount_Favorite.text = fav.totalGivenToCause.toString()
            itemView.tv_cause_Favorite.text = fav.cause
            itemView.tv_numOfDonations_Favorite.text = fav.numTimesDonatedTo.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_favorite_cause, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favorites[position])
    }
}