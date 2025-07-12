package com.example.feasto_25.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.Home_fragment.Home
import com.example.feasto_25.R

class Login_page : AppCompatActivity() {
    lateinit var button: Button
    lateinit var et_mobile : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, Home::class.java))
            finish()
            return
        }


        et_mobile = findViewById(R.id.et_mobile)
        button = findViewById(R.id.btn_continue)


        button.setOnClickListener {
            val mobile = et_mobile.text.toString().trim()

            if (mobile.isEmpty()) {
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show()
            } else if (!mobile.matches(Regex("^[6-9]\\d{9}$"))) {
                Toast.makeText(this, "Enter a valid Indian mobile number", Toast.LENGTH_SHORT).show()
            } else {

                sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                val fullMobile = "+91$mobile"
                val intent = Intent(this, Home::class.java)
                intent.putExtra("MOBILE_NUMBER", fullMobile)
                startActivity(intent)
                finish()
            }
        }
    }
}