package com.muhammetkdr.mvvm_attemp_to_learn.screens.searchnews

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews.BreakingNewsViewModel

class SearchNewsViewModelFactory (val app: Application, private val searchNewsRepository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchNewsViewModel::class.java)){
            return SearchNewsViewModel(app,searchNewsRepository) as T
        }else{
            throw IllegalStateException("Can not create instance of searchViewModel")
        }
    }
}
