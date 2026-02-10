package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityArticleWebViewBinding
import com.google.android.material.snackbar.Snackbar

class ArticleWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleWebViewBinding
    private val fallbackWebViewClient = WebViewClient()
    private val fallbackWebChromeClient = WebChromeClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBackHandling()
        setupWebView()
        loadArticle()
    }

    private fun setupToolbar() {
        binding.toolbar.title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
            .ifBlank { getString(R.string.article) }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBackHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupWebView() {
        with(binding.webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.linearProgressIndicator.progress = newProgress
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.linearProgressIndicator.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    private fun loadArticle() {
        val url = intent.getStringExtra(EXTRA_URL).orEmpty()
        if (url.isBlank()) {
            Snackbar.make(binding.root, R.string.invalid_article_url, Snackbar.LENGTH_SHORT).show()
            finish()
            return
        }
        binding.webView.loadUrl(url)
    }

    override fun onDestroy() {
        binding.webView.apply {
            stopLoading()
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            webViewClient = fallbackWebViewClient
            webChromeClient = fallbackWebChromeClient
            destroy()
        }
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_URL = "extra_url"
        private const val EXTRA_TITLE = "extra_title"

        fun newIntent(context: Context, url: String, title: String): Intent {
            return Intent(context, ArticleWebViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_TITLE, title)
            }
        }
    }
}
