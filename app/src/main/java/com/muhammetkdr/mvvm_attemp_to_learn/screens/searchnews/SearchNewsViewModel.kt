package com.muhammetkdr.mvvm_attemp_to_learn.screens.searchnews

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammetkdr.mvvm_attemp_to_learn.models.NewsResponse
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.screens.NewsApplication
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchNewsViewModel(app:Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews : LiveData<Resource<NewsResponse>> get() = _searchNews
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean> get() = _isLoading

    fun searchNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        safeBreakingNewsCall(searchQuery)
//        _searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
//        _searchNews.postValue(handleSearchNewsResponse(response))
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
                    val olduArticles = searchNewsResponse?.articles
                    val newsArticles = resultResponse.articles
                    olduArticles?.addAll(newsArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(searchQuery: String){
        _searchNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery,searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                _searchNews.postValue(Resource.Error("No Internet Connection!"))
            }
        }catch(t:Throwable){
            when(t){
                is IOException -> _searchNews.postValue(Resource.Error("Network Failure!"))
                else -> _searchNews.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun setLoadingDataFalse(){
        _isLoading.value = false
    }

    fun setLoadingDataTrue(){
        _isLoading.value = true
    }

}