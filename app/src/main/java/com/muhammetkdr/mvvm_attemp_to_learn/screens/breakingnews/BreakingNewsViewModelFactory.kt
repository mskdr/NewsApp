package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository

class BreakingNewsViewModelFactory(val app: Application,private val newsRepository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BreakingNewsViewModel::class.java)){
            return BreakingNewsViewModel(app,newsRepository) as T
        }else{
            throw IllegalStateException("Can not create instance of breakingNewsViewModel")
        }
    }
}