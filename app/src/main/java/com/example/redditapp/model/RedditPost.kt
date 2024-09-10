package com.example.redditapp.model

import com.google.gson.annotations.SerializedName

data class RedditPost(@SerializedName("title") val title: String,
                      val author: String,
                      val url: String,
                      val thumbnail:String?,
                      val created_utc: Long,
                      val num_comments: Int)
