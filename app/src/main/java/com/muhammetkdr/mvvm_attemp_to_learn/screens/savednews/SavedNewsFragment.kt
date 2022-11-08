package com.muhammetkdr.mvvm_attemp_to_learn.screens.savednews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentSavedNewsBinding

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private var _binding : FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedNewsViewModel : SavedNewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSavedNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        savedNewsViewModel = ViewModelProvider(this)[SavedNewsViewModel::class.java]
        newsAdapter.setOnItemClickListener { article->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragment2ToArticleFragment(article)
            Navigation.findNavController(view).navigate(action)
        }

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}