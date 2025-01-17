package com.example.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.dataClasses.OtherData

class MyFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)
        view.findViewById<TextView>(R.id.title).text = arguments?.getString("title")
        view.findViewById<TextView>(R.id.description).text = arguments?.getString("description")
        Glide.with(this@MyFragment).load(arguments?.getString("image")).into(view.findViewById(R.id.image))

        return view
    }

    companion object {
        fun createNewInstance(data: OtherData): MyFragment {
            val fragment = MyFragment()
            fragment.apply {
                arguments = Bundle().apply {
                    putString("title", data.title)
                    putString("description", data.description)
                    putString("url", data.url)
                    putString("image", data.image)
                    putString("content", data.content)
                    putString("publishedAt", data.publishedAt)
                }
            }
            return fragment
        }
    }
}