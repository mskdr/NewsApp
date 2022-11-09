package com.muhammetkdr.mvvm_attemp_to_learn.screens.savednews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository

class SavedNewsViewModelFactory (private val savedNewsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SavedNewsViewModel::class.java)){
            return SavedNewsViewModel(savedNewsRepository) as T
        }else{
            throw IllegalStateException("Can not create instance of savedNewsViewModel")
        }
    }
}