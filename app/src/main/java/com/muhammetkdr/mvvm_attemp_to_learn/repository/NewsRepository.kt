package com.muhammetkdr.mvvm_attemp_to_learn.repository

import com.muhammetkdr.mvvm_attemp_to_learn.api.RetrofitInstance

class NewsRepository() {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
}