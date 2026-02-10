package com.example.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(NewsUiState())
    val uiState: LiveData<NewsUiState> = _uiState

    init {
        loadNews()
    }

    fun searchNews(query: String) {
        loadNews(query = query.trim())
    }

    fun clearSearch() {
        loadNews(query = "")
    }

    fun refreshNews() {
        loadNews(isRefresh = true)
    }

    private fun loadNews(
        query: String = _uiState.value?.query.orEmpty(),
        isRefresh: Boolean = false
    ) {
        val currentState = _uiState.value ?: NewsUiState()
        val shouldShowLoader = !isRefresh &&
            (currentState.articles.isEmpty() || currentState.query != query)

        _uiState.value = currentState.copy(
            query = query,
            isLoading = shouldShowLoader,
            isRefreshing = isRefresh,
            errorMessage = null
        )

        viewModelScope.launch {
            repository.fetchNews(query)
                .onSuccess { articles ->
                    _uiState.value = (_uiState.value ?: NewsUiState()).copy(
                        articles = articles,
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = (_uiState.value ?: NewsUiState()).copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = throwable.message
                            ?: "Unable to load news right now. Please try again."
                    )
                }
        }
    }
}
