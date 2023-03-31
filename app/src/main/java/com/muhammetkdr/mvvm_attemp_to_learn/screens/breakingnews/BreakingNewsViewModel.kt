package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
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

class BreakingNewsViewModel(app: Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    private val _breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews : LiveData<Resource<NewsResponse>> get() = _breakingNews
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean> get() = _isLoading

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch(Dispatchers.IO) {
        safeBreakingNewsCall(countryCode)
//        _breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
//        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }
                                        // response = cevap / karşılık
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }else{
                 val oldArticles = breakingNewsResponse?.articles
                 val newsArticles = resultResponse.articles
                    oldArticles?.addAll(newsArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        _breakingNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                _breakingNews.postValue(Resource.Error("No Internet Connection!"))
            }
        }catch(t:Throwable){
            when(t){
                is IOException -> _breakingNews.postValue(Resource.Error("Network Failure!"))
                else ->_breakingNews.postValue(Resource.Error("Conversion Error!"))
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
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
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