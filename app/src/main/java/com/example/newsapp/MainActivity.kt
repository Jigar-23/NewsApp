package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.adapters.MyFragmentAdapter
import com.example.newsapp.adapters.MyRecyclerAdapter
import com.example.newsapp.dataClasses.NewsData
import com.example.newsapp.dataClasses.OtherData
import com.example.newsapp.service.ApiService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadData()
        setBottomNav()
    }
    private fun setTabLayout(viewPager: ViewPager2) {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.tabMode=TabLayout.MODE_FIXED
        tabLayout.tabGravity=TabLayout.GRAVITY_FILL
        TabLayoutMediator(tabLayout, viewPager){tab,pos->
            if(pos==0){
                tab.text="HeadLines"
            }
            if(pos==1){
                tab.text="National"
            }
            if(pos==2){
                tab.text="Sports"
            }
        }.attach()
    }

    private fun setBottomNav() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.home -> {
                    true
                }

                else -> {
                    false
                }
            }


        }
    }

    private fun loadData(): ArrayList<OtherData> {
        var arrayList = ArrayList<OtherData>()
        Retrofit.Builder().baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java).getData().enqueue(object : Callback<NewsData> {
                override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                    if (response.isSuccessful) {
                        arrayList = response.body()?.articles!!
                        createViewFromData(arrayList)
                    } else {
                        Log.d("response unsuccessful", "--")
                    }
                }

                override fun onFailure(call: Call<NewsData>, t: Throwable) {

                }
            })

        return arrayList
    }

    private fun createViewFromData(arrayList: ArrayList<OtherData>) {
        lifecycleScope.launch(Dispatchers.Main) {
            val data = ArrayList<ArrayList<OtherData>>()
            data.add(arrayList)
            for (i in 0 until 2) data.add(ArrayList())
            val viewPager = findViewById<ViewPager2>(R.id.viewPagerMain)
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.adapter = MyFragmentAdapter(this@MainActivity, data)
            setTabLayout(viewPager)

        }
    }


}