package com.example.top10downloaderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class TitleAdapter(private val myTitle: ArrayList<FeedEntry>):  RecyclerView.Adapter<TitleAdapter.ItemViewHolder>(){
    class ItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val list1 =myTitle[position]

        holder.itemView.apply {
            tvTitle.text = list1.name
        }
    }

    override fun getItemCount() = myTitle.size
}