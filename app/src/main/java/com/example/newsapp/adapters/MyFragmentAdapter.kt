package com.example.newsapp.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.dataClasses.OtherData
import com.example.newsapp.fragments.MyFragment

class MyFragmentAdapter(fragmentActivity: FragmentActivity, private val data: ArrayList<OtherData>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        Log.d("data_size",data.size.toString())
        return data.size
    }
    override fun createFragment(position: Int): Fragment {
        return MyFragment.createNewInstance(data[position])
    }
}

