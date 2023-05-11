package com.example.myapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class ImageAdapter (private val images: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]
        holder.progressBar.visibility = View.VISIBLE // wyświetlenie ProgressBar
        Glide.with(holder.imageView.context)
            .load(imageUri).apply(RequestOptions().override(100, 100))
            .addListener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Obsługa błędu ładowania obrazka
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Zaktualizuj widok, kiedy obrazek zostanie pobrany
                    holder.progressBar.visibility = View.GONE
                    holder.imageView.setImageDrawable(resource)
                    return true
                }
            })
            .into(holder.imageView)
        holder.itemView.setOnClickListener { onItemClick(imageUri) }

    }

    override fun getItemCount() = images.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}