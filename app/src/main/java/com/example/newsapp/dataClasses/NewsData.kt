package com.example.newsapp.dataClasses

data class NewsData(
    val articles:ArrayList<OtherData>
)

data class OtherData (
    val title:String,
    val description:String,
    val content:String,
    val url:String,
    val image:String,
    val publishedAt:String
)