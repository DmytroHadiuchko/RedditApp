package com.example.redditapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.adapter.PostAdapter
import com.example.redditapp.model.RedditResponse
import com.example.redditapp.network.RedditClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RedditClient.retrofit.getTopPosts(10).enqueue(object : Callback<RedditResponse> {
            override fun onResponse(call: Call<RedditResponse>, response: Response<RedditResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.data?.children?.map { it.data } ?: emptyList()
                    recyclerView.adapter = PostAdapter(posts)
                }
            }

            override fun onFailure(call: Call<RedditResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
            }
        })
    }
}