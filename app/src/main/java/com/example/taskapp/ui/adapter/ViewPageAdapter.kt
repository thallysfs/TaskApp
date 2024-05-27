package com.example.taskapp.ui.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: MutableList<androidx.fragment.app.Fragment> = ArrayList()
    private val titleList: MutableList<Int> = ArrayList()

    fun getTitle(position: Int): Int {
        return titleList[position]
    }

    fun addFragment(fragment: androidx.fragment.app.Fragment, title: Int){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun createFragment(position: Int): androidx.fragment.app.Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}