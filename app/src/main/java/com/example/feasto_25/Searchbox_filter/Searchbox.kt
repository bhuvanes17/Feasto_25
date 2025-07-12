package com.example.feasto_25.Searchbox_filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.R
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.Recyclerview.FoodAdapter
import com.example.feasto_25.Recyclerview.Fooditem
import android.text.TextWatcher
class Searchbox : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var noResultsText: TextView

    private val foodList = listOf(
        Fooditem("All", R.drawable.dish_offer),
        Fooditem("Poori", R.drawable.poori),
        Fooditem("Dosa", R.drawable.dosa),
        Fooditem("Pongal", R.drawable.pongal),
        Fooditem("Idli", R.drawable.idli),
        Fooditem("Fried rice", R.drawable.fired_rice),
        Fooditem("Vada", R.drawable.vada),
        Fooditem("Chicken", R.drawable.chicken),
        Fooditem("Paneer", R.drawable.paneer),
        Fooditem("Briyani", R.drawable.biryani),
        Fooditem("Pizza", R.drawable.pizza),
        Fooditem("Cake", R.drawable.cake),
        Fooditem("Burger", R.drawable.burger2),
        Fooditem("Juice", R.drawable.juicy)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_searchbox)

     //   val editText = findViewById<EditText>(R.id.etSearch2)
       // val recyclerView = findViewById<RecyclerView>(R.id.rv_dish)
        editText = findViewById(R.id.etSearch2)
        recyclerView = findViewById(R.id.rv_dish)
        noResultsText = findViewById(R.id.tvNoResults)




        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = FoodAdapter(foodList, R.layout.category_list_large, showIndicator = false) { categoryName ->
            val intent = Intent(this, Searchfilter::class.java)
            intent.putExtra("SELECTED_DISH", categoryName)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

       /*
        Handler(Looper.getMainLooper()).postDelayed({
            val foodList = listOf(
                Fooditem("All", R.drawable.dish_offer),
                Fooditem("Poori", R.drawable.poori),
                Fooditem("Dosa", R.drawable.dosa),
                Fooditem("Pongal", R.drawable.pongal),
                Fooditem("Idli", R.drawable.idli),
                Fooditem("Fried rice", R.drawable.fired_rice),
                Fooditem("Vada", R.drawable.vada),
                Fooditem("Chicken", R.drawable.chicken),
                Fooditem("Paneer", R.drawable.paneer),
                Fooditem("Briyani", R.drawable.biryani),
                Fooditem("Pizza", R.drawable.pizza),
                Fooditem("Cake", R.drawable.cake),
                Fooditem("Burger", R.drawable.burger2),
                Fooditem("Juice", R.drawable.juicy)
            )

            recyclerView.layoutManager = GridLayoutManager(this, 3,RecyclerView.VERTICAL,false)
        recyclerView.adapter = FoodAdapter(foodList, R.layout.category_list_large,showIndicator = false) { categoryName ->
                //Toast.makeText(this, "Clicked: ${selectedItem.name}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Searchfilter::class.java)
            intent.putExtra("SELECTED_DISH", categoryName)
            startActivity(intent)
            }

        }, 10)*/

        editText.requestFocus()

        editText.post {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = if (!query.isNullOrEmpty()) {
                    foodList.filter { it.name.contains(query, ignoreCase = true) }
                } else {
                    foodList
                }

                adapter = FoodAdapter(filteredList, R.layout.category_list_large, showIndicator = false) { categoryName ->
                    val intent = Intent(this@Searchbox, Searchfilter::class.java)
                    intent.putExtra("SELECTED_DISH", categoryName)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter

                noResultsText.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

}
