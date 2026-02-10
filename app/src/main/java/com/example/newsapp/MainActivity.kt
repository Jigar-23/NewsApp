package com.example.newsapp

import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.api.NewsApiService
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.NewsAdapter
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(
            repository = NewsRepository(
                apiService = NewsApiService.create()
            )
        )
    }

    private val newsAdapter = NewsAdapter(::openArticle)
    private var lastShownError: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        binding.toolbar.title = getString(R.string.app_name)

        binding.articlesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
            setHasFixedSize(true)
        }

        binding.searchButton.setOnClickListener {
            runSearch()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            viewModel.clearSearch()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                runSearch()
                true
            } else {
                false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNews()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this) { state ->
            binding.loadingProgress.isVisible = state.isLoading
            binding.swipeRefresh.isRefreshing = state.isRefreshing

            newsAdapter.submitList(state.articles)
            renderEmptyState(state.articles.isEmpty(), state.query, state.errorMessage)
            renderError(state.errorMessage)
        }
    }

    private fun renderEmptyState(
        isListEmpty: Boolean,
        query: String,
        errorMessage: String?
    ) {
        val shouldShow = isListEmpty && !binding.loadingProgress.isVisible && !binding.swipeRefresh.isRefreshing
        binding.emptyStateText.isVisible = shouldShow

        if (!shouldShow) return

        binding.emptyStateText.text = when {
            !errorMessage.isNullOrBlank() -> getString(R.string.error_loading_news)
            query.isNotBlank() -> getString(R.string.no_search_results, query)
            else -> getString(R.string.no_news_available)
        }
    }

    private fun renderError(errorMessage: String?) {
        if (errorMessage.isNullOrBlank()) {
            lastShownError = null
            return
        }
        if (errorMessage == lastShownError) return

        lastShownError = errorMessage
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private fun openArticle(article: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        if (browserIntent.resolveActivity(packageManager) != null) {
            startActivity(browserIntent)
        } else {
            Snackbar.make(binding.root, R.string.no_browser_found, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun runSearch() {
        hideKeyboard()
        viewModel.searchNews(binding.searchEditText.text?.toString().orEmpty())
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }
}
