package com.example.projetomobiledef.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.MutableLiveData
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(private val items: MutableList<SymbolSummary>):RecyclerView.Adapter<HomeRecyclerAdapter.ItemViewHolder>(){

    private var clickListener: SymbolCLicked?=null

    class ItemViewHolder(itemView:View,):RecyclerView.ViewHolder(itemView){
        val logoImageView:AppCompatImageView=itemView.findViewById(R.id.ivSymbolImage_summary)
        val stockName:TextView=itemView.findViewById(R.id.tvSymbol_summary)
        val stockPrice:TextView=itemView.findViewById(R.id.tvSymbolPrice_summary)
        val stockPercent:TextView=itemView.findViewById(R.id.tvSymbolPercent_summary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.stock_card_summary,parent,false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val list=items[position]

        items[position].logo_url.let{
            Picasso.get().load(it).into(holder.logoImageView)
        }
        holder.itemView.setOnClickListener {
            clickListener?.onSymbolCLicked(list)
        }

        holder.stockName.text = items[position].symbol
        holder.stockPercent.text = items[position].change_percent.toString()
        holder.stockPrice.text = items[position].current_price.toString()
    }

    fun setDetailsClickListener(listener:HomeRecyclerAdapter.SymbolCLicked){
        clickListener=listener
    }

    interface SymbolCLicked {
        fun onSymbolCLicked(symbol: SymbolSummary){}
    }

    fun updateData(newData: List<SymbolSummary>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}



