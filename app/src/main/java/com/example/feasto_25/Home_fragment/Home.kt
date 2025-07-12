package com.example.feasto_25.Home_fragment

import android.R.attr.fragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.feasto_25.R
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity(){
    @SuppressLint("RestrictedApi")
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var progressBar: ProgressBar


    private val deliveryFragment = DeliveryFragment()
    private val historyFragment = HistoryFragment()
    private var isFirstTimeDelivery = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        progressBar = findViewById(R.id.progressBar)
        // Set default fragment
        //openFragment(DeliveryFragment())



        supportFragmentManager.beginTransaction()
            .add(R.id.framelayout, deliveryFragment, "DELIVERY")
            .add(R.id.framelayout, historyFragment, "HISTORY")
            .hide(historyFragment)
            .hide(deliveryFragment)
            .commit()

        showFragmentWithProgress(deliveryFragment)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    if (isFirstTimeDelivery) {
                        showFragmentWithProgress(deliveryFragment)
                        isFirstTimeDelivery = false
                    } else {
                        showFragment(deliveryFragment)
                    }
                }

                R.id.bottom_history -> {
                    showFragment(historyFragment)
                }
            }
            true
        }



    }

    private fun showFragmentWithProgress(fragment: Fragment) {
        progressBar.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            showFragment(fragment)
            progressBar.visibility = View.GONE
        }, 1200)
    }

    private fun showFragment(fragmentToShow: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment == fragmentToShow) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }




}


/* loadFragmentWithDelay(DeliveryFragment())

       // Handle navigation item selection
       bottomNavigationView.setOnItemSelectedListener { item ->
           when (item.itemId) {
               R.id.bottom_home -> openFragment(DeliveryFragment())
               R.id.bottom_history -> openFragment(HistoryFragment())
           }
           true
       }*/


/* private fun loadFragmentWithDelay(fragment: Fragment) {
      // Show progress bar
      progressBar.visibility = View.VISIBLE

      // Delay for 700ms before loading fragment
      Handler(Looper.getMainLooper()).postDelayed({
          supportFragmentManager.beginTransaction()
              .replace(R.id.framelayout, fragment)
              .commit()

          // Hide progress bar after fragment loads
          progressBar.visibility = View.GONE
      }, 700) // You can change this delay (ms) if needed
  }

 fun openFragment(fragment: Fragment) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.framelayout, fragment)
          .commit()
  }*/