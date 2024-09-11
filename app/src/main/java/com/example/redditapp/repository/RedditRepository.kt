package com.example.redditapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.redditapp.api.RedditApiService
import com.example.redditapp.model.RedditPost
import com.example.redditapp.model.RedditResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RedditRepository(private val apiService: RedditApiService) {

    private val TAG = "RedditRepository"

    fun getTopPosts(limit: Int): LiveData<List<RedditPost>> {
        val postLiveData = MutableLiveData<List<RedditPost>>()

        apiService.getTopPosts(limit).enqueue(object : Callback<RedditResponse> {
            override fun onResponse(
                call: Call<RedditResponse>,
                response: Response<RedditResponse>,
            ) {
                if (response.isSuccessful) {
                    val posts = response.body()?.data?.children?.map { it.data } ?: emptyList()
                    Log.d(TAG, "API response success, fetched ${posts.size} posts")
                    postLiveData.postValue(posts)
                } else {
                    Log.e(TAG, "API response failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(
                call: Call<RedditResponse>,
                t: Throwable,
            ) {
                Log.e(TAG, "Error fetching posts: ${t.message}", t)
            }
        })
        return postLiveData
    }
}
