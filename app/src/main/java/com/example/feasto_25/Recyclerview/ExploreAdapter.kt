package com.example.feasto_25.Recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.R

class ExploreAdapter(private val items: List<Exploreitem>) :
    RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgExplore: ImageView = itemView.findViewById(R.id.explor_img)
        val tvExploreName: TextView = itemView.findViewById(R.id.explore_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_list, parent, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val item = items[position]
        holder.imgExplore.setImageResource(item.imageResId)
        holder.tvExploreName.text = item.name
    }

    override fun getItemCount(): Int = items.size
}
