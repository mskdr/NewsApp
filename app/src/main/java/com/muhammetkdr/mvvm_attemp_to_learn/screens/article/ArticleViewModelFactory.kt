package com.muhammetkdr.mvvm_attemp_to_learn.screens.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository

class ArticleViewModelFactory (private val articlesRepository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArticleViewModel::class.java)){
            return ArticleViewModel(articlesRepository) as T
        }else{
            throw IllegalStateException("Can not create instance of articleViewModel")
        }
    }
}