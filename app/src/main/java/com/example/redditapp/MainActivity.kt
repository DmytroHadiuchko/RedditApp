package com.example.redditapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.adapter.PostAdapter
import com.example.redditapp.api.RedditClient
import com.example.redditapp.repository.RedditRepository
import com.example.redditapp.viewModel.RedditViewModel
import com.example.redditapp.viewModel.RedditViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: RedditViewModel
    private lateinit var recyclerView: RecyclerView
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Requesting WRITE_EXTERNAL_STORAGE permission")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = RedditRepository(RedditClient.retrofit)
        val viewModelFactory = RedditViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(RedditViewModel::class.java)

        viewModel.posts.observe(this) { posts ->
            Log.d(TAG, "Received ${posts.size} posts")
            recyclerView.adapter = PostAdapter(posts)
        }

        viewModel.fetchTopPosts(10)
    }
}
