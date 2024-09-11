package com.example.redditapp.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object RedditClient {
    private const val BASE_URL = "https://www.reddit.com/"
    private val TAG = "RedditClient"

    val retrofit: RedditApiService by lazy {
        Log.d(TAG, "Initializing Retrofit client")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditApiService::class.java)
    }
}
