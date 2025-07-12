package com.example.feasto_25.Location_details

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.R

class Address_book : AppCompatActivity() {

    lateinit var myaddressdown: ImageView
    lateinit var address: RelativeLayout
    private lateinit var optionlist: ImageView
    private lateinit var addressbook: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_address_book)

        address = findViewById(R.id.rl_myaddress)
        myaddressdown = findViewById(R.id.img_myaddre_downlogo)
        optionlist = findViewById(R.id.img_option2)
        addressbook = findViewById(R.id.tv_addressbook2)





        myaddressdown.setOnClickListener {
            closewindow()

        }

        address.setOnClickListener {
            val intent = Intent(this, Myaddress::class.java)
            startActivity(intent)
        }

        optionlist.setOnClickListener {
            val optionMenu = PopupMenu(this, optionlist)
            optionMenu.menuInflater.inflate(R.menu.option, optionMenu.menu)

            optionMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        //   Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()

                       val currentAddress = addressbook.text.toString()

                        val sharedPref = getSharedPreferences("additional_Location_info", MODE_PRIVATE)
                        sharedPref.edit {
                            putString("additional_Delivery_info", currentAddress)
                        }


                        val intent = Intent(this, Myaddress::class.java)
                        intent.putExtra("from_edit_menu", true)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            optionMenu.show()
        }

    }

    override fun onResume() {
        super.onResume()
        updatehome_address()
    }

    private fun updatehome_address() {
        val sharedPref = getSharedPreferences("additional_Location_info", MODE_PRIVATE)
        val addressinfo = sharedPref.getString("additional_Delivery_info", "")

        val addressBook = findViewById<TextView>(R.id.tv_addressbook2)
        addressBook.text = addressinfo

        val sharedPref2 = getSharedPreferences("UserData", MODE_PRIVATE)
        val mobile = sharedPref2.getString("mobile", "")

        val addressphone = findViewById<TextView>(R.id.tv_address_phone)
        addressphone.text = mobile
    }
}



@Suppress("DEPRECATION")
private fun Address_book.closewindow() {
    finish() // closes Address activity
    overridePendingTransition(0, R.anim.slide_down)
}
