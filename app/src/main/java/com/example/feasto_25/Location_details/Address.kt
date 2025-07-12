package com.example.feasto_25.Location_details

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.Home_fragment.DeliveryFragment
import com.example.feasto_25.Home_fragment.Home
import com.example.feasto_25.R
import androidx.core.content.edit

class Address : AppCompatActivity() {

    private lateinit var downarrow: ImageView
    private lateinit var optionlist: ImageView

    private lateinit var delivery_address: TextView
    private lateinit var add_address: TextView
    private lateinit var addressBook: TextView
    private lateinit var addressphone: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_address)

        delivery_address = findViewById(R.id.tv_delivery_address)
        addressBook = findViewById(R.id.tv_address2)
        optionlist = findViewById(R.id.img_option)
         addressphone = findViewById(R.id.tv_address_phone)



        add_address =findViewById(R.id.tv_add_address)

        add_address.setOnClickListener {

            val intent = Intent(this, Myaddress::class.java)
            startActivity(intent)
        }

        downarrow = findViewById(R.id.img_addre_downlogo)
        downarrow.setOnClickListener {

            closewindow()
        }

        optionlist.setOnClickListener {
            val optionMenu = PopupMenu(this, optionlist)
            optionMenu.menuInflater.inflate(R.menu.option, optionMenu.menu)

            optionMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                     //   Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()

                        val currentAddress = addressBook.text.toString()

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


        updatefragment()

    }

    override fun onResume() {
        super.onResume()
        updateAddressUI()
        updatehome_addresss()


    }

    private fun updatehome_addresss() {
        val sharedPref3 = getSharedPreferences("additional_Location_info", MODE_PRIVATE)
        val addressinfo = sharedPref3.getString("additional_Delivery_info", "")

        addressBook.text = addressinfo

        val sharedPref2 = getSharedPreferences("UserData", MODE_PRIVATE)
        val mobile = sharedPref2.getString("mobile", "")

        addressphone.text = mobile
    }

    private fun updateAddressUI() {

        val sharedPref = getSharedPreferences("Location_info", MODE_PRIVATE)
        val address = sharedPref.getString("Delivery info", "")

        delivery_address.text = address
    }


    @Suppress("DEPRECATION")

    private fun updatefragment() {
        val currentCard = findViewById<CardView>(R.id.card_curren_location)
        val savedCard = findViewById<CardView>(R.id.card_save_addre)

        currentCard.setOnClickListener {
            val selected = delivery_address.text.toString()
            Log.d("AddressActivity", "Current location clicked. Selected address: $selected")
            saveAndReturn(selected)
        }

        savedCard.setOnClickListener {
            val selected = addressBook.text.toString()
            Log.d("AddressActivity", "Saved address clicked. Selected address: $selected")
            saveAndReturn(selected)
        }
    }
    @Suppress("DEPRECATION")
    private fun saveAndReturn(selected: String) {
        val sharedPref = getSharedPreferences("Location_info2", MODE_PRIVATE)
        sharedPref.edit { putString("Delivery info2", selected) }

        val intent = Intent()
        intent.putExtra("selected_address", selected)
        setResult(RESULT_OK, intent)
        finish()
        overridePendingTransition(0, R.anim.slide_down)
    }

    @Suppress("DEPRECATION")
    private fun Address.closewindow() {
        finish() // closes Address activity
        overridePendingTransition(0, R.anim.slide_down)

    }
}

