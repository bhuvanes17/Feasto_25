package com.example.feasto_25.Home_fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.feasto_25.Profile_logout.Logout_activity
import com.example.feasto_25.R
import com.example.feasto_25.Recyclerview.ExploreAdapter
import com.example.feasto_25.Recyclerview.FilterAdapter
import com.example.feasto_25.Recyclerview.FoodAdapter
import com.example.feasto_25.databinding.FragmentDeliveryBinding
import com.example.feasto_25.databinding.FragmentHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding? = null

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
       // return inflater.inflate(R.layout.fragment_history, container, false)
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val profileTextView = view?.findViewById<TextView>(R.id.tv_nav_draw)
        /*requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.framelayout, DeliveryFragment())
                .commit()

            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.bottom_home
        }*/

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.bottom_home  // This triggers the listener and navigates cleanly
        }

        binding?.tvNavDraw?.setOnClickListener {
            val intent = Intent(activity, Logout_activity::class.java)
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
        updatetextviewUI()

    }

    private fun updatetextviewUI() {
        val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPref.getString("first_name", "") ?: ""
        val firstLetter = if (firstName.isNotEmpty()) firstName[0].uppercaseChar().toString() else "?"


        binding?.tvNavDraw?.text = firstLetter
    }


    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}