package com.muhammetkdr.mvvm_attemp_to_learn.screens.savednews

import android.content.ClipData.Item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.muhammetkdr.mvvm_attemp_to_learn.R
import com.muhammetkdr.mvvm_attemp_to_learn.adapter.NewsAdapter
import com.muhammetkdr.mvvm_attemp_to_learn.databinding.FragmentSavedNewsBinding
import com.muhammetkdr.mvvm_attemp_to_learn.repository.NewsRepository
import com.muhammetkdr.mvvm_attemp_to_learn.roomdb.ArticleDatabase

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

        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val savedNewsViewModelFactory = SavedNewsViewModelFactory(newsRepository)
        savedNewsViewModel = ViewModelProvider(this,savedNewsViewModelFactory)[SavedNewsViewModel::class.java]

        newsAdapter.setOnItemClickListener { article->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragment2ToArticleFragment(article)
            Navigation.findNavController(view).navigate(action)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                savedNewsViewModel.deleteArticle(article)
                Snackbar.make(view,"Article deleted successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        savedNewsViewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        savedNewsViewModel.getSavedNews().observe(viewLifecycleOwner){ articles->
            newsAdapter.differ.submitList(articles.toList())
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