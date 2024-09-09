package com.example.redditapp.model

data class RedditPost(val title: String,
                      val author: String,
                      val url: String,
                      val thumbnail:String?,
                      val created_utc: Long,
                      val num_comments: Int)
