package com.example.newsapp.data.repository

import com.example.newsapp.data.api.NewsApiService
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NewsRepository(
    private val apiService: NewsApiService
) {

    suspend fun fetchNews(query: String): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getArticles(search = query.takeIf { it.isNotBlank() })
            Result.success(response.results.map { it.toDomainModel() })
        } catch (throwable: Throwable) {
            Result.failure(normalizeError(throwable))
        }
    }

    private fun normalizeError(error: Throwable): Throwable {
        return when (error) {
            is IOException -> Throwable("No internet connection. Check your network and try again.")
            is HttpException -> Throwable("Server error ${error.code()}. Please try again.")
            else -> Throwable("Unable to load news right now. Please try again.")
        }
    }
}
