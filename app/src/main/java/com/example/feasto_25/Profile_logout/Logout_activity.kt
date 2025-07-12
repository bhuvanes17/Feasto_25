package com.example.feasto_25.Profile_logout

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.Location_details.Address
import com.example.feasto_25.Location_details.Address_book
import com.example.feasto_25.Login.Login_page
import com.example.feasto_25.R

class Logout_activity : AppCompatActivity() {

    private lateinit var profilename: TextView
    private lateinit var username: TextView
    private lateinit var addressbook: LinearLayout
    private lateinit var logout : LinearLayout
    private lateinit var firstletter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout)


        logout = findViewById(R.id.ll_logout)
        username = findViewById(R.id.tv_username)

        addressbook = findViewById(R.id.ll_addressbook)
        profilename = findViewById(R.id.tv_editname)


        logout.setOnClickListener {
            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            val intent = Intent (this, Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        profilename.setOnClickListener {

            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
        addressbook.setOnClickListener {
            val intent = Intent(this, Address_book::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in,
                R.anim.slide_out
            )
            startActivity(intent, options.toBundle())
        }
    }

    override fun onResume() {
        super.onResume()
        updatetextviewUI()
    }

    private fun updatetextviewUI() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val fullName = sharedPref.getString("first_name", "") ?: ""
        val firstLetter = if (fullName.isNotEmpty()) fullName[0].uppercaseChar().toString() else "?"


        val etfirstletter = findViewById<TextView>(R.id.tv_1stletter_log)
        etfirstletter.text = firstLetter
        username.text = fullName



    }
}