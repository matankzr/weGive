package com.example.wegive.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wegive.R
import kotlinx.android.synthetic.main.item_person.view.*
import kotlinx.android.synthetic.main.item_store.view.*

class PersonAdapter(val context: Context, val persons: List<Person>,val clickListener: (Person)->Unit): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount() = persons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(persons[position], clickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(person: Person, clickListener: (Person)->Unit) {
            itemView.tv_personId.text = person.id
            itemView.setOnClickListener { clickListener(person) }
        }
    }
}