package com.example.redditapp.api

import com.example.redditapp.model.RedditResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApiService {
    @GET("/r/androiddev/top.json")
    fun getTopPosts(@Query("limit") limit: Int) : Call<RedditResponse>
}
