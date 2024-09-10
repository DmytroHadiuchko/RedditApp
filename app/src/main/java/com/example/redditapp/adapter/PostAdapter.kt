package com.example.redditapp.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.redditapp.databinding.ItemPostBinding
import com.example.redditapp.model.RedditPost
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.text.isNotEmpty

class PostAdapter(private val posts: List<RedditPost>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>()  {

        class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
            val titleTextView: TextView = binding.titleTextView
            val authorTextView: TextView = binding.authorTextView
            val thumbnailImageView: ImageView = binding.thumbnailImageView
            val createdTextView: TextView = binding.createdTextView
            val commentsTextView: TextView = binding.commentsTextView
            val saveButton : Button = binding.saveButton
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
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
            holder.saveButton.visibility = View.VISIBLE

            Glide.with(holder.itemView.context)
                .load(post.thumbnail)
                .into(holder.thumbnailImageView)


            holder.thumbnailImageView.setOnClickListener {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.thumbnail))
                holder.itemView.context.startActivity(intent)
            }

            holder.saveButton.setOnClickListener {
                saveImageToGallery(holder.itemView.context, post.thumbnail)
            }
        } else {
            holder.thumbnailImageView.visibility = View.GONE
            holder.saveButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = posts.size

    private fun saveImageToGallery(context: Context, imageUrl: String) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedImageURL = saveBitmapToGallery(context, resource)
                    if (savedImageURL != null) {
                        Toast.makeText(context, "The image was saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "The image wasn't saved", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun saveBitmapToGallery(context : Context, bitmap: Bitmap) : String? {
        val fileName = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUrl: String? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = uri?.let { resolver.openOutputStream(it) }
                imageUrl = uri?.toString()
            } else {
                val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imageDir, fileName)
                fos = FileOutputStream(image)
                imageUrl = image.absolutePath
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaScannerConnection.scanFile(context, arrayOf(imageUrl), null) {path, uri ->
                    Log.d("MediaScanner", "Scanned $path: $uri")
                }
            }

        } catch (e: Exception) {
            Log.e("MediaScanner", "Error saving image", e)
        } finally {
            fos?.close()
        }
        return imageUrl
    }
}