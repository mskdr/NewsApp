package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentBreakingNewsBinding
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.muhammetkdr.mvvm_attemp_to_learn.utils.Resource


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private var _binding: FragmentBreakingNewsBinding? = null
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
        val breakingNewsViewModelFactory = BreakingNewsViewModelFactory(requireActivity().application,newsRepository)
        breakingNewsViewModel = ViewModelProvider(this,breakingNewsViewModelFactory)[BreakingNewsViewModel::class.java]

        breakingNewsViewModel.breakingNews.observe(viewLifecycleOwner){ response->
           when(response){
               is Resource.Success -> {
                   hideProgressBar()
                   response.data?.let { newsResponse ->
                       newsAdapter.differ.submitList(newsResponse.articles.toList())
                       val totalPages = (newsResponse.totalResults / QUERY_PAGE_SIZE) + 2
                       isLastPage = breakingNewsViewModel.breakingNewsPage == totalPages
                       if(isLastPage){
                           binding.rvBreakingNews.setPadding(0,0,0,0)
                       }
                   }
               }
               is Resource.Error -> {
                   hideProgressBar()
                   response.message?.let { message ->
                       Log.e(TAG,"An error occured :  $message")
                       Toast.makeText(activity,"An error occured: $message",Toast.LENGTH_LONG).show()
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
//        breakingNewsViewModel.setLoadingDataFalse()
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
//        breakingNewsViewModel.setLoadingDataTrue()
        isLoading = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                breakingNewsViewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }



    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            binding.rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}