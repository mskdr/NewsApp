package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammetkdr.mvvm_attemp_to_learn.models.NewsResponse
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BreakingNewsViewModel() : ViewModel() {

    val newsRepository = NewsRepository()

    private val _breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews : LiveData<Resource<NewsResponse>> get() = _breakingNews
    private var breakingNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}