package com.example.feasto_25.Information_Restaurant

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feasto_25.Food_details.FoodinfoAdapter
import com.example.feasto_25.Food_details.Foodinfoitem
import com.example.feasto_25.R
import com.example.feasto_25.Recyclerview.Fooditem
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.feasto_25.Recyclerview.Restaurant_item
import com.google.firebase.firestore.FirebaseFirestore
import com.example.feasto_25.Food_details.OnQuantityChangeListener
import com.google.firebase.firestore.Source


class Restaurant_Details : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodinfoAdapter: FoodinfoAdapter
    private val food_list = mutableListOf<Foodinfoitem>()
    private lateinit var progressBar: ProgressBar
    private lateinit var fullContentLayout: View



    //val restRating = intent.getStringExtra("restRating") ?: "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_details)

        progressBar = findViewById(R.id.progressBar)
        fullContentLayout = findViewById(R.id.cl_full_layout)

        progressBar.visibility = View.VISIBLE
        fullContentLayout.visibility = View.GONE

        val popup = findViewById<RelativeLayout>(R.id.rl_itempopup)
       // popup.visibility = View.GONE


        val restId = intent.getStringExtra("restId") ?: ""
        val restName = intent?.getStringExtra("restName") ?: "Unknown Restaurant"
        val restRating = intent?.getStringExtra("restRating") ?: ""

        val restaurantname = findViewById<TextView>(R.id.tv_restaurant_name)
        restaurantname.text = restName

        val tvratingvalue = findViewById<TextView>(R.id.tv_ratingvalue)
        tvratingvalue.text = restRating

        val ratingbar = findViewById<RatingBar>(R.id.rating_restaurant2)
        val ratingValue = restRating.toFloatOrNull() ?: 0f
        ratingbar.rating= ratingValue


        val imgOffer = findViewById<ImageView>(R.id.img_offerlogo)
        val tvOffer = findViewById<TextView>(R.id.tv_offers)
        val offerLayout = findViewById<LinearLayout>(R.id.ll_layoutoffer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val headerLayout = findViewById<LinearLayout>(R.id.llhedertext)

        /*foodinfoAdapter = FoodinfoAdapter(this, food_list)
        recyclerView = findViewById<RecyclerView>(R.id.rv_food_details)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = foodinfoAdapter*/

        recyclerView = findViewById<RecyclerView>(R.id.rv_food_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        foodinfoAdapter = FoodinfoAdapter(this, food_list, object : OnQuantityChangeListener {
            override fun onQuantityChanged(totalQuantity: Int) {
                val numberText = findViewById<TextView>(R.id.tv_number)

                if (totalQuantity > 0) {
                    if (popup.visibility != View.VISIBLE) {
                        popup.visibility = View.VISIBLE
                        val slideUp = AnimationUtils.loadAnimation(this@Restaurant_Details, R.anim.slide_in)
                        popup.startAnimation(slideUp)
                    }
                    numberText.text = totalQuantity.toString()
                } else {
                    val slideDown = AnimationUtils.loadAnimation(this@Restaurant_Details, R.anim.slide_down)
                    popup.startAnimation(slideDown)

                    // Hide view after animation ends
                    slideDown.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}
                        override fun onAnimationRepeat(animation: Animation?) {}
                        override fun onAnimationEnd(animation: Animation?) {
                            popup.visibility = View.GONE
                        }
                    })                }
            }
        })
        recyclerView.adapter = foodinfoAdapter
        fetchFoodItemsFromFirestore(restId)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        collapsingToolbar.title = " "

        var titleShown = false

        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            headerLayout.translationY = -scrollY.toFloat()

            val fadeStart = 0
            val fadeEnd = 130
            val alpha = 1f - (scrollY - fadeStart).toFloat() / (fadeEnd - fadeStart)
            val clampedAlpha = alpha.coerceIn(0f, 1f)
            headerLayout.alpha = clampedAlpha

            if (clampedAlpha <= 0.05f && !titleShown) {
                collapsingToolbar.title = restaurantname.text

                val params = headerLayout.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 70 // for example, 40px
                headerLayout.layoutParams = params

                titleShown = true
            } else if (clampedAlpha > 0.05f && titleShown) {
                collapsingToolbar.title = " "
                val params = headerLayout.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 70 // for example, 40px
                headerLayout.layoutParams = params
                titleShown = false
            }
        }


        val offers = listOf(
            Pair(R.drawable.offers_icon, "20% OFF up to ₹50"),
            Pair(R.drawable.delivery_bike, "Free Delivery on ₹149+"),
            Pair(R.drawable.lock, "Free Delivery")
        )

        var index = 1

        imgOffer.setImageResource(offers[0].first)
        tvOffer.text = offers[0].second
        offerLayout.alpha = 1f

        fun animateOffer() {
            offerLayout.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    // Update content
                    imgOffer.setImageResource(offers[index].first)
                    tvOffer.text = offers[index].second

                    // Animate fade in
                    offerLayout.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start()

                    // Prepare next index
                    index = (index + 1) % offers.size

                    // Repeat after delay
                    offerLayout.postDelayed({ animateOffer() }, 2500)
                }.start()
        }
        offerLayout.postDelayed({ animateOffer() }, 2500)


    }

    private fun fetchFoodItemsFromFirestore(restId: String) {
        val fullContentLayout = findViewById<ConstraintLayout>(R.id.cl_full_layout)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        db.collection("foodorder")
            .document(restId)
            .collection("food_item")
            .get(Source.SERVER)
            .addOnSuccessListener { result ->
                Log.d("FIREBASE_DATA", "Total Documents: ${result.size()}")
                for (doc in result) {
                    val foodName = doc.getString("food_name") ?: ""
                    val description = doc.getString("description") ?: ""
                    val price = doc.getString("price") ?: ""
                    val img = doc.getString("img") ?: ""


                    val food = Foodinfoitem(
                        food_name = foodName,
                        description = description,
                        img = img,
                        price = price
                    )
                    food_list.add(food)
                }
                foodinfoAdapter.notifyDataSetChanged()
                progressBar.postDelayed({
                    progressBar.visibility = View.GONE
                    fullContentLayout.visibility = View.VISIBLE
                }, 1000)
            }
            .addOnFailureListener {
                Toast.makeText(this, "No internet. Please check your connection.", Toast.LENGTH_LONG).show()

            }

    }




}



















// setSupportActionBar(toolbar)
//supportActionBar?.setDisplayHomeAsUpEnabled(true)

/* val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
 collapsingToolbar.title = "Anjappar Hotel"

 val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
 val textView = findViewById<LinearLayout>(R.id.llhedertext)

 nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
     textView.translationY = -scrollY.toFloat()
     val fadeStart = 0
     val fadeEnd = 300
     val alpha = 1f - (scrollY - fadeStart).toFloat() / (fadeEnd - fadeStart)
     textView.alpha = alpha.coerceIn(0f, 1f)
 }*/