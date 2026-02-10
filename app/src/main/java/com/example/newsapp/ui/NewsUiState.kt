package com.example.newsapp.ui

import com.example.newsapp.data.model.Article

data class NewsUiState(
    val query: String = "",
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)
