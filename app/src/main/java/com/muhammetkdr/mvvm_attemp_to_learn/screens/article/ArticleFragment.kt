package com.muhammetkdr.mvvm_attemp_to_learn.screens.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentArticleBinding
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel : ArticleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val articleViewModelFactory = ArticleViewModelFactory(newsRepository)
        articleViewModel = ViewModelProvider(this,articleViewModelFactory)[ArticleViewModel::class.java]

        arguments?.let {
            val args = ArticleFragmentArgs.fromBundle(it)
            args.let { articleFragmentArgs->
                binding.webView.apply {
                    webViewClient = WebViewClient()
                    loadUrl(articleFragmentArgs.article.url!!)
                }

                articleViewModel.getSavedNews().observe(viewLifecycleOwner){ articleList->
                    articleList?.let {
                        for (article in articleList){
                            val isTheSame = article.url.equals(articleFragmentArgs.article.url)
                            if(isTheSame){
                                binding.fab.visibility = View.INVISIBLE
                            }
                        }
                    }
                }

                binding.fab.setOnClickListener{
                    articleViewModel.saveArticle(args.article)
                    Snackbar.make(view,"Article saved succesfully",Snackbar.LENGTH_LONG).apply {
                        setAction("undo"){
                            articleViewModel.deleteArticle(args.article)
                            binding.fab.visibility = View.VISIBLE
                        }
                    }
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}