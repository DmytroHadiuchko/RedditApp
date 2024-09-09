package com.example.redditapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redditapp.R
import com.example.redditapp.model.RedditPost
import kotlin.text.isNotEmpty

class PostAdapter(private val posts: List<RedditPost>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>()  {

        class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            val authorTextView: TextView = view.findViewById(R.id.authorTextView)
            val thumbnailImageView: ImageView = view.findViewById(R.id.thumbnailImageView)
            val createdTextView: TextView = view.findViewById(R.id.createdTextView)
            val commentsTextView: TextView = view.findViewById(R.id.commentsTextView)
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.titleTextView.text = post.title
        holder.authorTextView.text = "Author: ${post.author}"

        val currentTime = System.currentTimeMillis() / 1000
        val timeDifference = currentTime - post.created_utc
        val hoursAgo = timeDifference / 3600
        holder.createdTextView.text = "$hoursAgo hours ago"

        holder.commentsTextView.text = "Comments: ${post.num_comments}"

        if (post.thumbnail != null && post.thumbnail.isNotEmpty() && post.thumbnail != "self") {
            holder.thumbnailImageView.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(post.thumbnail)
                .into(holder.thumbnailImageView)


            holder.thumbnailImageView.setOnClickListener {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.thumbnail))
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.thumbnailImageView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = posts.size
}