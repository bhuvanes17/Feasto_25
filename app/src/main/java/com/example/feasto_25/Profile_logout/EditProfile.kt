package com.example.feasto_25.Profile_logout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.R

class EditProfile : AppCompatActivity() {
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var mobilnum: EditText
    private lateinit var email: EditText
    private lateinit var btnupdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        firstname =findViewById(R.id.et_firstname)
        lastname = findViewById(R.id.et_lastname)
        email = findViewById(R.id.et_email)
        mobilnum =findViewById(R.id.et_mobilenum)
        btnupdate = findViewById(R.id.btn_update)


        btnupdate.setOnClickListener {
            updateUserInfo()
        }

    }

    private fun updateUserInfo(){

        val fname = firstname.text.toString().trim()
        val lname = lastname.text.toString().trim()
        val mobile = mobilnum.text.toString().trim()
        val emailText = email.text.toString().trim()

        var isValid = true

        if (fname.isEmpty()) {
            firstname.error = "First name is required"
            isValid = false
        }

        if (lname.isEmpty()) {
            lastname.error = "Last name is required"
            isValid = false
        }

        if (mobile.isEmpty()) {
            mobilnum.error = "Mobile number is required"
            isValid = false
        }

        if (emailText.isEmpty()) {
            email.error = "Email is required"
            isValid = false
        }

        if (isValid) {
            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear() // ✅ Clear previous data
            editor.putString("first_name", fname)
            editor.putString("last_name", lname)
            editor.putString("mobile", mobile)
            editor.putString("email", emailText)
            editor.apply()

            firstname.text.clear()
            lastname.text.clear()
            mobilnum.text.clear()
            email.text.clear()

            Toast.makeText(this, "User info updated successfully", Toast.LENGTH_LONG).show()
        }

    }
}

