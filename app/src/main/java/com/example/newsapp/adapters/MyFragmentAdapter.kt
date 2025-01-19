package com.example.newsapp.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.R
import com.example.newsapp.dataClasses.OtherData
import com.example.newsapp.fragments.MyFragment

class MyFragmentAdapter(
    private val fragmentActivity: FragmentActivity,
    private val data: ArrayList<ArrayList<OtherData>>
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {

        return MyFragment.createNewInstance(data[position],fragmentActivity)
    }


}

