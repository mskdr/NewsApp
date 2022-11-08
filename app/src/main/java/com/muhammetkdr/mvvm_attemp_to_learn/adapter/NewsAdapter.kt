package com.muhammetkdr.mvvm_attemp_to_learn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.NewsOrderBinding
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article
import com.muhammetkdr.mvvm_attemp_to_learn.models.NewsResponse

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallBack)

    inner class NewsViewHolder(val newsBinding : NewsOrderBinding): ViewHolder(newsBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsItemBinding = NewsOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(newsItemBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.newsBinding.ivArticleImage)
            holder.newsBinding.tvSource.text = article.source?.name
            holder.newsBinding.tvTitle.text = article?.title
            holder.newsBinding.tvDescription.text = article?.description
            holder.newsBinding.tvPublishedAt.text = article?.publishedAt
            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    private var onItemClickListener: ((Article)-> Unit)? = null

    fun setOnItemClickListener(listener: (Article)-> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}