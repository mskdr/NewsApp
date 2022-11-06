package com.muhammetkdr.mvvm_attemp_to_learn.screens.breakingnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentBreakingNewsBinding
import com.muhammetkdr.mvvm_attemp_to_learn.models.Article
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var breakingNewsViewModel: BreakingNewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

//        val xx = breakingNewsViewModel.getBreakingNews("us")
//        val newsRepository = NewsRepository()  // data...
//        val breakingNewsViewModelFactory = BreakingNewsViewModelFactory(newsRepository)

        breakingNewsViewModel = ViewModelProvider(this).get(BreakingNewsViewModel::class.java)

        breakingNewsViewModel.breakingNews.observe(viewLifecycleOwner){ response->
            response.data?.let { newsResponse ->
                // adapter .. newsAdapter.differ.submitList(newResponse.articles)
                newsAdapter.differ.submitList(newsResponse.articles)
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}