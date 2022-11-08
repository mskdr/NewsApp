package com.muhammetkdr.mvvm_attemp_to_learn.screens.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.upsert(article)
    }

    init {
        getSavedNews()
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    }