package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentBreakingNewsBinding
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Resource


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private var _binding: FragmentBreakingNewsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var breakingNewsViewModel: BreakingNewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private val TAG = "BreakingNewsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBreakingNewsBinding.inflate(layoutInflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener { article->
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
            Navigation.findNavController(view).navigate(action)
        }

        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val breakingNewsViewModelFactory = BreakingNewsViewModelFactory(newsRepository)
        breakingNewsViewModel = ViewModelProvider(this,breakingNewsViewModelFactory)[BreakingNewsViewModel::class.java]

        breakingNewsViewModel.breakingNews.observe(viewLifecycleOwner){ response->
           when(response){
               is Resource.Success -> {
                   hideProgressBar()
                   response.data?.let { newsResponse ->
                       newsAdapter.differ.submitList(newsResponse.articles.toList())
                   }
               }
               is Resource.Error -> {
                   hideProgressBar()
                   response.message?.let { message ->
                       Log.e(TAG,"An error occured :  $message")
                   }
               }
               is Resource.Loading -> {
                   showProgressBar()
               }
           }
        }
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        breakingNewsViewModel.setLoadingDataFalse()
//        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        breakingNewsViewModel.setLoadingDataTrue()
//        isLoading = true
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}