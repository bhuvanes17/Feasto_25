package com.example.feasto_25.Home_fragment

import android.Manifest
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feasto_25.Information_Restaurant.Restaurant_Details
import com.example.feasto_25.Location_details.Address
import com.example.feasto_25.Profile_logout.Logout_activity
import com.example.feasto_25.R
import com.example.feasto_25.Recyclerview.ExploreAdapter
import com.example.feasto_25.Recyclerview.Exploreitem
import com.example.feasto_25.Recyclerview.FilterAdapter
import com.example.feasto_25.Recyclerview.Filteritem
import com.example.feasto_25.Recyclerview.FoodAdapter
import com.example.feasto_25.Recyclerview.Fooditem
import com.example.feasto_25.Recyclerview.RestaurantAdapter
import com.example.feasto_25.Recyclerview.Restaurant_item
import com.example.feasto_25.Searchbox_filter.Searchbox
import com.example.feasto_25.databinding.FragmentDeliveryBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView // ✅ Add this
import com.google.firebase.firestore.Source
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeliveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeliveryFragment : Fragment() {

    var binding: FragmentDeliveryBinding? = null
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var filterAdapter: FilterAdapter

    private lateinit var restaurantAdapter: RestaurantAdapter
    private val itemList = mutableListOf<Restaurant_item>()
    private val db = FirebaseFirestore.getInstance()

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001



    var appBarLayoutBehavior: AppBarLayout.Behavior? = null

    private var foodList = listOf(
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
        Fooditem("Juice", R.drawable.juicy),
    )
    val filterOptions = listOf(
        Filteritem("Filter", hasIcons = true, iconStart = R.drawable.filter_list, iconEnd = R.drawable.down_arrow),
        Filteritem("Under Rs.150"),
        Filteritem("Under 30 min"),
        Filteritem("Rating 4.0+"),
        Filteritem("Pure Veg")
    )
    val exploreList = listOf(
        Exploreitem("Offers", R.drawable.price_tag),
        Exploreitem("Top 10", R.drawable.star_icon),
        Exploreitem("Food on train", R.drawable.train_icon),
        Exploreitem("Collection", R.drawable.food_icon),
        Exploreitem("Gift cards", R.drawable.gift_card)
    )


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_delivery, container, false)

        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)



        binding?.tvProfile2?.setOnClickListener {
           //Log.d("VIEW", "tvProfile2: ${binding?.tvProfile2}")
           val intent = Intent(requireActivity(), Logout_activity::class.java)
           startActivity(intent)
       }

        binding?.etSearch?.setOnClickListener {
            val intent = Intent(requireActivity(), Searchbox::class.java)
            startActivity(intent)
        }


        binding?.imAddress?.setOnClickListener {

           /* val intent = Intent(requireContext(), Address::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.slide_in,
                R.anim.slide_out
            )
            startActivity(intent,options.toBundle())*/

            checkLocationPermissionAndProceed()

        }


          /* Glide.with(this)
                .load(R.drawable.food_del)
                .into(binding?.imgGifBanner!!)*/

          /* foodAdapter = FoodAdapter(foodList)
            binding?.rvFoodcategory?.layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            binding?.rvFoodcategory?.adapter=foodAdapter*/


        foodAdapter = FoodAdapter(foodList, R.layout.category_list) { selectedCategory ->
            filterRestaurantsByCategory(selectedCategory)
            if (selectedCategory != "All") {
                binding?.appBarLayout?.setExpanded(false, true)

                bottomNav.animate()
                    .translationY(bottomNav.height.toFloat())
                    .setDuration(250)
                    .withEndAction { bottomNav.visibility = View.GONE }
                    .start()
                binding?.tvExplorelist?.visibility = View.GONE
                binding?.rvExplorelist?.visibility = View.GONE


            }
            else {

                binding?.appBarLayout?.setExpanded(true, true)

                bottomNav.visibility = View.VISIBLE
                bottomNav.animate()
                    .translationY(0f)
                    .setDuration(250)
                    .start()
                binding?.tvExplorelist?.visibility = View.VISIBLE
                binding?.rvExplorelist?.visibility = View.VISIBLE

            }
            binding?.appBarLayout?.requestLayout() // Request layout for the AppBarLayout
        }


        binding?.rvFoodcategory?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.rvFoodcategory?.setHasFixedSize(true);
        binding?.rvFoodcategory?.adapter = foodAdapter
        filterRestaurantsByCategory("All")


            filterAdapter = FilterAdapter(filterOptions)
            binding?.rvFilterlist?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding?.rvFilterlist?.adapter = filterAdapter

            val adapter = ExploreAdapter(exploreList)
            binding?.rvExplorelist?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding?.rvExplorelist?.adapter = adapter


        restaurantAdapter = RestaurantAdapter(requireContext(), itemList) { docId, name,rating ->
            val intent = Intent(requireContext(), Restaurant_Details::class.java)
            intent.putExtra("restId", docId)
            intent.putExtra("restName", name)
            intent.putExtra("restRating", rating)
            startActivity(intent)
        }
        binding!!.rvRestaurants.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding!!.rvRestaurants.adapter = restaurantAdapter

        //fetchdatafirebas()


    }


    private fun DeliveryFragment.filterRestaurantsByCategory(category: String) {
        if (category == "All") {
            fetchdatafirebas()
        } else {
            binding?.fragmentProgressBar?.visibility = View.VISIBLE
            itemList.clear()
            restaurantAdapter.notifyDataSetChanged()

            val categoryToQuery = category.lowercase()
            val startTime = System.currentTimeMillis()

            db.collection("foodorder")
                .whereEqualTo("category.${categoryToQuery}", true)
                .get(Source.SERVER)
                .addOnSuccessListener { documents ->
                    Log.d("DEBUG_FIRESTORE", "Matched count: ${documents.size()}")
                    for (doc in documents) {
                        Log.d("DEBUG_FIRESTORE", "Restaurant: ${doc.getString("name")}")
                        val item = Restaurant_item(
                            imgurl = doc.getString("img_url") ?: "",
                            name = doc.getString("name") ?: "",
                            rating = doc.getString("rating") ?: "0",
                            docId = doc.id
                        )
                        itemList.add(item)
                    }
                     restaurantAdapter.notifyDataSetChanged()

                    val timeElapsed = System.currentTimeMillis() - startTime
                    val minDisplayTime = 500L
                    val delayDuration = if (timeElapsed < minDisplayTime) minDisplayTime - timeElapsed else 0L

                    lifecycleScope.launch {
                        delay(delayDuration) // Apply calculated delay
                        binding?.fragmentProgressBar?.visibility = View.GONE
                    }


                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "No internet. Please check your connection.", Toast.LENGTH_SHORT).show()
                    binding?.fragmentProgressBar?.visibility = View.GONE
                }
        }
    }




    private fun fetchdatafirebas() {
        db.collection("foodorder")
            .get(Source.SERVER)
            .addOnSuccessListener { documents ->
                itemList.clear()
                for (doc in documents) {
                    val item = Restaurant_item(
                        imgurl = doc.getString("img_url") ?: "",
                        name = doc.getString("name") ?: "",
                        rating = doc.getString("rating") ?: "0",
                        docId = doc.id
                    )
                    itemList.add(item)
                }
                restaurantAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "No internet. Please check your connection.", Toast.LENGTH_LONG).show()
                binding?.fragmentProgressBar?.visibility = View.GONE
            }
    }


    override fun onResume() {
        super.onResume()
        updateAddressUI()
        updatetextviewUI()

    }

    private fun updatetextviewUI() {
        val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPref.getString("first_name", "") ?: ""
        val firstLetter = if (firstName.isNotEmpty()) firstName[0].uppercaseChar().toString() else "?"

        binding?.tvProfile2?.text = firstLetter
       /* val profileTextView = view?.findViewById<TextView>(R.id.tv_profile2)
        profileTextView?.text = firstLetter */

    }

    private fun updateAddressUI() {

        val selectedAddress = requireContext()
            .getSharedPreferences("Location_info2", Context.MODE_PRIVATE)
            .getString("Delivery info2", null)

        binding?.tvLocationInfo?.text = selectedAddress ?: "No address selected"
    }

    val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            navigateToAddressActivity()
        } else {
            showPermissionDialogAgain()
        }
    }

    private fun showPermissionDialogAgain() {
        AlertDialog.Builder(requireContext())
            .setTitle("Location Permission Needed")
            .setMessage("Please allow location access to proceed.")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                checkLocationPermissionAndProceed() // re-try permission request
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun checkLocationPermissionAndProceed() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            navigateToAddressActivity()
        } else {
            locationPermissionLauncher.launch(permission)
        }
    }

    private fun navigateToAddressActivity() {
        val intent = Intent(requireContext(), Address::class.java)
        val options = ActivityOptions.makeCustomAnimation(
            requireContext(),
            R.anim.slide_in,
            R.anim.slide_out
        )
        startActivity(intent, options.toBundle())
    }



}





