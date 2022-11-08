package com.muhammetkdr.mvvm_attemp_to_learn.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}

