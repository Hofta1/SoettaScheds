package com.example.assignmentindividu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.assignmentindividu.databinding.ActivityHomePageBinding
import com.example.assignmentindividu.fragment.ArrivalFragment
import com.example.assignmentindividu.fragment.DepartureFragment
import com.example.assignmentindividu.fragment.ProfileFragment
import com.example.assignmentindividu.fragment.TransactionFragment
import com.example.assignmentindividu.fragment.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class HomePage : AppCompatActivity() {
    private lateinit var homeBinder: ActivityHomePageBinding
    private lateinit var viewpager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {


        homeBinder = ActivityHomePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(homeBinder.root)

        setUpTabs()


    }


    private fun setUpTabs(){
        viewpager = homeBinder.viewPager
        tabLayout = homeBinder.tabLayout
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DepartureFragment(), "Departure")
        adapter.addFragment(ArrivalFragment(), "Arrival")
        adapter.addFragment(TransactionFragment(),"Transaction")
        adapter.addFragment(ProfileFragment(),"Profile")
        viewpager.adapter = adapter
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.la_plane_departure)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.la_plane_arrival)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.baseline_shopping_cart_24)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.baseline_account_circle_24)
    }
}