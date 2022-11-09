package com.muhammetkdr.mvvm_attemp_to_learn.models

import com.muhammetkdr.mvvm_attemp_to_learn.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)