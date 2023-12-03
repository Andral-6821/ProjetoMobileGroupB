package com.example.projetomobiledef.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.News
import com.squareup.picasso.Picasso

class NewsRecyclerAdapter(private val mNewsList: List<News>) : RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mNewsList[position]
        holder.NewsTitle.text = ItemsViewModel.title
        holder.NewsDescription.text = ItemsViewModel.description
        holder.NewsDate.text= ItemsViewModel.date
        ItemsViewModel.image_url.let{
            Picasso.get().load(it).into(holder.NewsImage)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mNewsList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val NewsTitle: TextView = itemView.findViewById(R.id.tvNews_title)
        val NewsDescription: TextView = itemView.findViewById(R.id.tvNews_description)
        val NewsDate: TextView= itemView.findViewById(R.id.tvNews_date)
        val NewsImage: ImageView = itemView.findViewById(R.id.ivNews_image)

    }
}