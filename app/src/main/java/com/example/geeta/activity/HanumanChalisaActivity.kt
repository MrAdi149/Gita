package com.example.geeta.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.geeta.databinding.ActivityHanumanChalisaBinding
import com.example.geeta.fragment.HanumanChalisaE
import com.example.geeta.fragment.HanumanChalisaH
import com.example.geeta.utils.Themes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HanumanChalisaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHanumanChalisaBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Themes.loadTheme(this)

        binding = ActivityHanumanChalisaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val fragmentList = listOf(HanumanChalisaH(), HanumanChalisaE())

        viewPager.adapter = TabPagerAdapter(this, fragmentList)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Hindi"
                1 -> tab.text = "English"
            }
        }.attach()
    }
    private inner class TabPagerAdapter(fragmentActivity: AppCompatActivity, private val fragments: List<Fragment>) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}