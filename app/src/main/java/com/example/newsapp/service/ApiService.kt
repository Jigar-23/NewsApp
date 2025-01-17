package com.example.newsapp.service

import com.example.newsapp.dataClasses.NewsData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("top-headlines?category=general&lang=en&country=us&max=10&apikey=9f55658f2d863c98a9c4a45763baaac7")
     fun getData(): Call<NewsData>

}