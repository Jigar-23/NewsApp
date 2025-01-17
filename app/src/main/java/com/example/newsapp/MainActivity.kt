package com.example.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.adapters.MyFragmentAdapter
import com.example.newsapp.dataClasses.NewsData
import com.example.newsapp.dataClasses.OtherData
import com.example.newsapp.service.ApiService
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

    }

    private fun loadData(): ArrayList<OtherData> {
        var arrayList = ArrayList<OtherData>()
        Retrofit.Builder().baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java).getData()
            .enqueue(object : Callback<NewsData> {
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
            val viewPager = findViewById<ViewPager2>(R.id.viewPagerMain)
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.adapter = MyFragmentAdapter(this@MainActivity, arrayList)
        }
    }
}