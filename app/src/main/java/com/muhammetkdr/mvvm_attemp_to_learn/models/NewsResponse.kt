package com.muhammetkdr.mvvm_attemp_to_learn.models

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)