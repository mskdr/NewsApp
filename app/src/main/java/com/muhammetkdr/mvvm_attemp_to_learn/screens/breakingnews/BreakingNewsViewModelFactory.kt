package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository

class BreakingNewsViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {

//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(BreakingNewsViewModel::class.java)){
//            return BreakingNewsViewModel(newsRepository) as T
//        }else{
//            throw IllegalStateException("Can not create instance of this viewModel")
//        }
//    }

}