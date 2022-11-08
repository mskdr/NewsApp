package com.muhammetkdr.mvvm_attemp_to_learn.screens.searchnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentSearchNewsBinding
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase
import com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews.BreakingNewsViewModelFactory
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private var _binding : FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchNewsViewModel : SearchNewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private val TAG = "SearchNewsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        searchNewsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsAdapter.setOnItemClickListener { article->
            val action = SearchNewsFragmentDirections.actionSearchNewsFragment2ToArticleFragment(article)
            Navigation.findNavController(view).navigate(action)
        }

        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val searchNewsViewModelFactory = SearchNewsViewModelFactory(newsRepository)
        searchNewsViewModel = ViewModelProvider(this,searchNewsViewModelFactory)[SearchNewsViewModel::class.java]

        searchNewsViewModel.searchNews.observe(viewLifecycleOwner){ response->
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
        searchNewsViewModel.setLoadingDataFalse()
//        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        searchNewsViewModel.setLoadingDataTrue()
//        isLoading = true
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}