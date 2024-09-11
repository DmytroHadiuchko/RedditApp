package com.example.redditapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.redditapp.repository.RedditRepository

class RedditViewModelFactory(private val repository: RedditRepository) : ViewModelProvider.Factory {

    private val TAG = "RedditViewModelFactory"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RedditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RedditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
