package com.muhammetkdr.mvvm_attemp_to_learn.repository

import com.muhammetkdr.mvvm_attemp_to_learn.api.RetrofitInstance
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) = RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) = RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}

