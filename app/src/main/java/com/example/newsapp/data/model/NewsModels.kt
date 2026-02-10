package com.example.newsapp.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class NewsResponse(
    val results: List<ArticleDto> = emptyList()
)

data class ArticleDto(
    val id: Long,
    val title: String,
    val url: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("news_site")
    val newsSite: String?,
    val summary: String?,
    @SerializedName("published_at")
    val publishedAt: String?
)

data class Article(
    val id: Long,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val source: String,
    val summary: String,
    val publishedAt: String
)

fun ArticleDto.toDomainModel(): Article {
    return Article(
        id = id,
        title = title,
        url = url,
        imageUrl = imageUrl,
        source = newsSite ?: "Unknown source",
        summary = summary?.takeIf { it.isNotBlank() } ?: "Summary unavailable.",
        publishedAt = formatPublishedDate(publishedAt)
    )
}

private fun formatPublishedDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) {
        return "Unknown date"
    }

    val datePart = rawDate.take(10)
    val input = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val output = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    return runCatching {
        val parsedDate = input.parse(datePart) ?: return@runCatching datePart
        output.format(parsedDate)
    }.getOrDefault(datePart)
}
