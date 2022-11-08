package com.muhammetkdr.mvvm_attemp_to_learn.screens.savednews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedNewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteArticle(article)
    }
}