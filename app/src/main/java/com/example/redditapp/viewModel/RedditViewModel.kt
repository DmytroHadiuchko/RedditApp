package com.example.redditapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redditapp.model.RedditPost
import com.example.redditapp.repository.RedditRepository

class RedditViewModel(private val repository: RedditRepository) : ViewModel() {

    private val _posts = MutableLiveData<List<RedditPost>>()
    val posts: LiveData<List<RedditPost>> = _posts
    private val TAG = "RedditViewModel"

    fun fetchTopPosts(limit: Int) {
        repository.getTopPosts(limit).observeForever {
            Log.d(TAG, "Posts fetched successfully, size: ${it.size}")
            _posts.value = it
        }
    }
}
