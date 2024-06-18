package com.example.assignmentindividu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.assignmentindividu.databinding.ActivityHomePageBinding
import com.example.assignmentindividu.fragment.ArrivalFragment
import com.example.assignmentindividu.fragment.DepartureFragment
import com.example.assignmentindividu.fragment.ProfileFragment
import com.example.assignmentindividu.fragment.TransactionFragment
import com.example.assignmentindividu.fragment.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import android.graphics.PorterDuff
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
        adapter.addFragment(TransactionFragment(),"Bookmark")
        adapter.addFragment(ProfileFragment(),"Profile")
        viewpager.adapter = adapter
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.la_plane_departure)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.la_plane_arrival)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.baseline_bookmark_border_24)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.baseline_account_circle_24)

        tabLayout.getTabAt(0)?.icon?.setColorFilter(androidx.appcompat.R.attr.colorPrimary, PorterDuff.Mode.SRC_IN)
        tabLayout.getTabAt(1)?.icon?.setColorFilter(androidx.appcompat.R.attr.colorPrimary, PorterDuff.Mode.SRC_IN)
        tabLayout.getTabAt(2)?.icon?.setColorFilter(androidx.appcompat.R.attr.colorPrimary, PorterDuff.Mode.SRC_IN)
        tabLayout.getTabAt(3)?.icon?.setColorFilter(androidx.appcompat.R.attr.colorPrimary, PorterDuff.Mode.SRC_IN)
        val unselectedColor = ContextCompat.getColor(this, R.color.unselected_tab_text)
        val selectedColor = ContextCompat.getColor(this, R.color.selected_tab_text)
        tabLayout.setTabTextColors(unselectedColor, selectedColor)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(unselectedColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // No action
            }
        })
    }
}