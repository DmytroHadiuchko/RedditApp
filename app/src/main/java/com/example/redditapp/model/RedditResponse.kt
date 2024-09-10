package com.example.redditapp.model

import com.google.gson.annotations.SerializedName

data class RedditResponse(@SerializedName("data") val data: RedditData)
