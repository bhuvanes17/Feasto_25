package com.example.feasto_25.Recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.R

class FoodAdapter(private val items: List<Fooditem>,
                  private val layoutResId: Int,
                  private val showIndicator: Boolean = true,
                  private val onCategoryClick: (String) -> Unit) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private var selectedPosition = 0
    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFood: ImageView = itemView.findViewById(R.id.imgFood)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
        val viewIndicator: View? = itemView.findViewById(R.id.viewIndicator)

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

               // notifyItemChanged(previousPosition)
                //notifyItemChanged(selectedPosition)
                notifyItemChanged(previousPosition,"indicator_only")
                notifyItemChanged(selectedPosition,"indicator_only")
                onCategoryClick(items[selectedPosition].name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int, payloads: MutableList<Any>) {
        /*if (payloads.isNotEmpty()) {
            holder.viewIndicator?.visibility =
                if (showIndicator && position == selectedPosition) View.VISIBLE else View.INVISIBLE

           // if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
        } else {
            onBindViewHolder(holder, position)
        }*/

       if (payloads.contains("indicator_only")) {
            holder.viewIndicator?.visibility =
                if (showIndicator && position == selectedPosition) View.VISIBLE else View.INVISIBLE
        } else {
            onBindViewHolder(holder, position)
        }

    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = items[position]
        holder.imgFood.setImageResource(item.imageResId)
        holder.tvFoodName.text = item.name

        // Optional: Highlight first item only
        holder.viewIndicator?.visibility =
            if (showIndicator && position == selectedPosition) View.VISIBLE else View.INVISIBLE
         //  if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int = items.size
}
