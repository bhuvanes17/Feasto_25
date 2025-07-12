package com.example.feasto_25.Searchbox_filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.R
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.Information_Restaurant.Restaurant_Details
import com.example.feasto_25.Recyclerview.FilterAdapter
import com.example.feasto_25.Recyclerview.Filteritem
import com.example.feasto_25.Recyclerview.RestaurantAdapter
import com.example.feasto_25.Recyclerview.Restaurant_item
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import okio.Source

class Searchfilter : AppCompatActivity() {

    //private var itemList: MutableList<Restaurant_item> = mutableListOf()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var filterAdapter: FilterAdapter

    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_searchfilter)

        val dishName = intent.getStringExtra("SELECTED_DISH") ?: ""
        val editText = findViewById<EditText>(R.id.etSearch3)
        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_restaurant_list)
        val recyclerView2 = findViewById<RecyclerView>(R.id.rv_filterinfo)

        editText.setText(dishName)
        editText.setSelection(dishName.length) // cursor at end

        textInputLayout.setEndIconOnClickListener {
            val intent = Intent(this, Searchbox::class.java)
            startActivity(intent)
        }

        val filterOptions = listOf(
            Filteritem("Filter", hasIcons = true, iconStart = R.drawable.filter_list, iconEnd = R.drawable.down_arrow),
            Filteritem("Under Rs.150"),
            Filteritem("Under 30 min"),
            Filteritem("Rating 4.0+"),
            Filteritem("Pure Veg")
        )

        filterAdapter = FilterAdapter(filterOptions)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter=filterAdapter

        db.collection("foodorder")
            .get(com.google.firebase.firestore.Source.SERVER)
            .addOnSuccessListener { result ->

                val itemList = mutableListOf<Restaurant_item>()

                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val imgurl = document.getString("img_url") ?: ""
                    val rating = document.getString("rating") ?: ""
                    val docId = document.id

                    val categoryMap = document.get("category") as? Map<String, Boolean> ?: emptyMap()
                    val dishList = categoryMap.filterValues { it }.keys.toList()

                    val restaurant = Restaurant_item(
                        name = name,
                        imgurl = imgurl,
                        rating = rating,
                        docId = docId,
                        dishes = dishList
                    )
                    itemList.add(restaurant)
                }

                // Now filter
                val filteredList = if (dishName.equals("All", ignoreCase = true)) {
                    itemList  // Show all
                } else {
                    itemList.filter { restaurant ->
                        restaurant.dishes.any { dish ->
                            dish.equals(dishName, ignoreCase = true)
                        }
                    }
                }

                Log.d("CheckData", "Filtered size: ${filteredList.size}")

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = RestaurantAdapter(this, filteredList) { docId, name, rating ->
                    val intent = Intent(this, Restaurant_Details::class.java)
                    intent.putExtra("restId", docId)
                    intent.putExtra("restName", name)
                    intent.putExtra("restRating", rating)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "No internet. Please check your connection.", Toast.LENGTH_LONG).show()
               // Log.e("Firestore", "Error fetching restaurants", e)
            }
    }
}