package com.example.redditapp.model

import com.google.gson.annotations.SerializedName

data class RedditData(@SerializedName("children") val children: List<RedditChildren>)
