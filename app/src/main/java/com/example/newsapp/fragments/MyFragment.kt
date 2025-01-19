package com.example.newsapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.adapters.MyRecyclerAdapter
import com.example.newsapp.dataClasses.OtherData

class MyFragment(
    private val arrayList: ArrayList<OtherData>,
    private val activity: FragmentActivity
) :
    Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)
        createRecyclerView(arrayList,view)
        return view
    }

    companion object {
        fun createNewInstance(data: ArrayList<OtherData>, context: FragmentActivity): MyFragment {
            return MyFragment(data, context)
        }
    }

    private fun createRecyclerView(arrayList: ArrayList<OtherData>, context: View) {
        val recyclerView = context.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view?.context)
        recyclerView.adapter = MyRecyclerAdapter(arrayList,activity)
    }
}