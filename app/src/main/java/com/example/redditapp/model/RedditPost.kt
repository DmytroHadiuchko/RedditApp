package com.example.redditapp.model

import com.google.gson.annotations.SerializedName

data class RedditPost(@SerializedName("title") val title: String,
                      @SerializedName("author") val author: String,
                      @SerializedName("url") val url: String,
                      @SerializedName("thumbnail") val thumbnail:String?,
                      @SerializedName("created_utc") val created_utc: Long,
                      @SerializedName("num_comments") val num_comments: Int)
