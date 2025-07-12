package com.example.feasto_25.Food_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feasto_25.R
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.util.TypedValueCompat.dpToPx
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FoodinfoAdapter
    (
    private val context: Context,
    private val fooditems: List<Foodinfoitem>,
    private val quantityListener: OnQuantityChangeListener
) : RecyclerView.Adapter<FoodinfoAdapter.FoodinfoViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodinfoAdapter.FoodinfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_details2, parent, false)
        return FoodinfoViewHolder(view,this)
    }


    override fun onBindViewHolder(holder: FoodinfoViewHolder, position: Int) {
        holder.bind(fooditems[position])
    }

    override fun getItemCount(): Int = fooditems.size


    fun getTotalQuantity(): Int {
        return fooditems.sumOf { it.quantity }
    }

    inner class FoodinfoViewHolder(itemView: View, private val adapter: FoodinfoAdapter) : RecyclerView.ViewHolder(itemView)
    {
        val foodname: TextView = itemView.findViewById(R.id.tv_foodname)
        val desc = itemView.findViewById<TextView>(R.id.tv_fooddescription)
        val price = itemView.findViewById<TextView>(R.id.tv_foodprice)
        val img = itemView.findViewById<ImageView>(R.id.img_foodphoto)
        val addBtn = itemView.findViewById<AppCompatButton>(R.id.btn_addfooditem)
        val llQuantity = itemView.findViewById<RelativeLayout>(R.id.ll_quantity)
        val tvQuantity = itemView.findViewById<TextView>(R.id.tv_quantity)
        val btnMinus = itemView.findViewById<ImageView>(R.id.btn_minus)
        val btnPlus = itemView.findViewById<ImageView>(R.id.btn_plus)



        fun bind(food: Foodinfoitem) {
            foodname.text = food.food_name
            price.text = if (food.price.isNullOrEmpty()) "Not available" else food.price

            if (food.description.isNullOrEmpty()) {
                desc.visibility = View.GONE

                // Move image below price if description is missing
                val imgParams = img.layoutParams as RelativeLayout.LayoutParams
                imgParams.addRule(RelativeLayout.BELOW, R.id.tv_foodprice)
                imgParams.topMargin = 10
                img.layoutParams = imgParams
            } else {
                desc.text = food.description
                desc.visibility = View.VISIBLE

                // Move image below description if available
                val imgParams = img.layoutParams as RelativeLayout.LayoutParams
                imgParams.addRule(RelativeLayout.BELOW, R.id.tv_fooddescription)
                imgParams.topMargin = 10
                img.layoutParams = imgParams
            }

            if (food.img.isNullOrEmpty()) {
                img.visibility = View.GONE

                val btnParams = addBtn.layoutParams as RelativeLayout.LayoutParams
                btnParams.addRule(RelativeLayout.BELOW, R.id.img_vegicon)
                btnParams.topMargin = 2
                addBtn.layoutParams = btnParams

                val llParams = llQuantity.layoutParams as RelativeLayout.LayoutParams
                llParams.addRule(RelativeLayout.BELOW, R.id.img_vegicon)
                llParams.topMargin = 2
                llQuantity.layoutParams = llParams

            } else {

                img.visibility = View.VISIBLE
                Glide.with(context).clear(img)
                img.setImageDrawable(null)
                // Load rounded image using Glide
                Glide.with(context)
                    .load(food.img)
                    .transform(RoundedCorners(20)) // px radius
                    .into(img)

                val imgParams = img.layoutParams as RelativeLayout.LayoutParams
                imgParams.addRule(RelativeLayout.BELOW,R.id.img_vegicon)
                imgParams.width = dpToPx(118)
                imgParams.height = dpToPx(325)
                imgParams.addRule(RelativeLayout.BELOW, R.id.img_vegicon)
                imgParams.marginEnd = dpToPx(12)
                imgParams.topMargin = dpToPx(-40)
                img.layoutParams = imgParams


            }

            if (food.quantity == 0) {
                addBtn.visibility = View.VISIBLE
                llQuantity.visibility = View.GONE
            } else {
                addBtn.visibility = View.GONE
                llQuantity.visibility = View.VISIBLE
                tvQuantity.text = food.quantity.toString()
            }

            updateQuantityUI(food)
            // Add button

            btnPlus.setOnClickListener {
                food.quantity++
                tvQuantity.text = food.quantity.toString()
                quantityListener.onQuantityChanged(adapter.getTotalQuantity())
            }

            btnMinus.setOnClickListener {
                if (food.quantity > 1) {
                    food.quantity--
                    tvQuantity.text = food.quantity.toString()
                    quantityListener.onQuantityChanged(adapter.getTotalQuantity())
                } else if (food.quantity == 1) {
                    food.quantity = 0
                    updateQuantityUI(food)  // Switch back to ADD button
                    quantityListener.onQuantityChanged(adapter.getTotalQuantity())
                }
            }

            addBtn.setOnClickListener {
                food.quantity = 1
                addBtn.visibility = View.GONE
                llQuantity.visibility = View.VISIBLE
                quantityListener.onQuantityChanged(adapter.getTotalQuantity())
                updateQuantityUI(food)
            }

        }
    }
    fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}



private fun FoodinfoAdapter.FoodinfoViewHolder.updateQuantityUI(food: Foodinfoitem) {

    if (food.quantity == 0) {
        addBtn.visibility = View.VISIBLE
        llQuantity.visibility = View.GONE
    } else {
        addBtn.visibility = View.GONE
        llQuantity.visibility = View.VISIBLE
        tvQuantity.text = food.quantity.toString()
    }
}


interface OnQuantityChangeListener {
    fun onQuantityChanged(totalQuantity: Int)
}
