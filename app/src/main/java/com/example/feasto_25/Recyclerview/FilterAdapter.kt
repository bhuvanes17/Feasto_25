package com.example.feasto_25.Recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.R

class FilterAdapter(private val items: List<Filteritem>) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStartIcon: ImageView = view.findViewById(R.id.im_start)
        val ivEndIcon: ImageView = view.findViewById(R.id.im_end)
        val tvLabel: TextView = view.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_list, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = items[position]
        holder.tvLabel.text = item.label

        if (item.hasIcons && item.iconStart != null) {
            holder.ivStartIcon.setImageResource(item.iconStart)
            holder.ivStartIcon.visibility = View.VISIBLE
        } else {
            holder.ivStartIcon.visibility = View.GONE
        }

        if (item.hasIcons && item.iconEnd != null) {
            holder.ivEndIcon.setImageResource(item.iconEnd)
            holder.ivEndIcon.visibility = View.VISIBLE
        } else {
            holder.ivEndIcon.visibility = View.GONE
        }
    }

    override fun getItemCount() = items.size
}
