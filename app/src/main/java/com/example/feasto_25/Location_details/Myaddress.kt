package com.example.feasto_25.Location_details

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.feasto_25.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class Myaddress : AppCompatActivity() {

    internal lateinit var googleMap: GoogleMap
    internal lateinit var fusedLocationClient: FusedLocationProviderClient
    internal lateinit var etAddress: EditText
    internal lateinit var et_addtional_Address: EditText

    internal var marker: Marker? = null
    internal val LOCATION_PERMISSION_REQUEST_CODE = 1001
    internal var isAdditionalAddressFocusedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_myaddress)


        etAddress = findViewById(R.id.etAddress)
        et_addtional_Address = findViewById(R.id.et_addtional_Address)
        val save_btn = findViewById<Button>(R.id.btnSave)


        val fromEditMenu = intent.getBooleanExtra("from_edit_menu", false)
        if (fromEditMenu) {
            val sharedPref = getSharedPreferences("additional_Location_info", MODE_PRIVATE)
            val addressInfo = sharedPref.getString("additional_Delivery_info", "")
            et_addtional_Address.setText(addressInfo)
        }



        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val fname = sharedPref.getString("first_name", "")
        val mobile = sharedPref.getString("mobile", "")

        val tvName = findViewById<TextView>(R.id.et_receiver_name)
        val tvMobile = findViewById<TextView>(R.id.et_receiver_number)

        tvName.text = fname
        tvMobile.text = mobile

        et_addtional_Address.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                isAdditionalAddressFocusedOnce = true
            }
        }


        save_btn.setOnClickListener {

            updateUserInfo()
            //Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show()
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            requestLocationPermission()

            googleMap.setOnMapClickListener { latLng ->
                marker?.remove()
                marker = googleMap.addMarker(
                    MarkerOptions().position(latLng).title("Selected Location")
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                getAddressFromLatLng(latLng)
            }
        }

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                googleMap.isMyLocationEnabled = true
                moveToCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

        }




private fun Myaddress.getAddressFromLatLng(latLng: LatLng) {
    try {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0].getAddressLine(0)
            etAddress.setText(address)
        } else {
            etAddress.setText("Address not found")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        etAddress.setText("Unable to get address")
    }


}

private fun Myaddress.requestLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    } else {
        googleMap.isMyLocationEnabled = true
        moveToCurrentLocation()
    }

}

private fun Myaddress.moveToCurrentLocation() {

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                marker?.remove()
                marker = googleMap.addMarker(
                    MarkerOptions().position(currentLatLng).title("You are here")
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                getAddressFromLatLng(currentLatLng)
            } else {
                Toast.makeText(this, "Couldn't fetch current location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun Myaddress.updateUserInfo() {

    val address = etAddress.text.toString().trim()
    val addtional_address = et_addtional_Address.text.toString().trim()
    val sharedPref = getSharedPreferences("Location_info", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.clear()
    editor.putString("Delivery info", address)
    editor.apply()

    if (isAdditionalAddressFocusedOnce) {
        if (addtional_address.isEmpty()) {
            et_addtional_Address.error = "Please enter the address"
            return
        } else {
            val sharedPref2 = getSharedPreferences("additional_Location_info", MODE_PRIVATE)
            sharedPref2.edit().apply {
                putString("additional_Delivery_info", addtional_address)
                apply()
            }
        }
    }

}




