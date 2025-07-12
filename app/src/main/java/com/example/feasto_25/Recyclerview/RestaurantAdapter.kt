package com.example.feasto_25.Recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feasto_25.R

class RestaurantAdapter(
    private val context: Context,
    private val items: List<Restaurant_item>,
    private val onClick: (String, String, String) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list, parent, false)
        return RestaurantAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantAdapterViewHolder, position: Int) {
        val item = items[position]

        Glide.with(context)
            .load(item.imgurl)
            .into(holder.image)

        holder.name.text = item.name
        holder.rating.text = item.rating

        val ratingValue = item.rating.toFloatOrNull() ?: 0f
        holder.ratingBar.rating = ratingValue

        holder.itemView.setOnClickListener {
            onClick(item.docId, item.name,item.rating) // or item.id if you have a Firestore document ID
        }
    }

    override fun getItemCount(): Int = items.size

    inner class RestaurantAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_rest)
        val name: TextView = itemView.findViewById(R.id.tv_res_name)
        val rating: TextView = itemView.findViewById(R.id.rating_res_value)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rating_res)


    }

}


