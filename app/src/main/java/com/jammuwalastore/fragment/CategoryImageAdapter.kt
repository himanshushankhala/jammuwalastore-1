package com.jammuwalastore.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jammuwalastore.R

class CategoryImageAdapter (
    private val context : Context,
    private val categoryImages : List <CategoryImage>
) : RecyclerView.Adapter<CategoryImageAdapter.ImageViewHolder>()
{

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val img = itemView.findViewById<ImageView>(R.id.image)
        val imgTitle = itemView.findViewById<TextView>(R.id.image_title);

        fun bindView(categoryImage: CategoryImage) {
            img.setImageResource(categoryImage.imageSrc)
            imgTitle.text = categoryImage.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_images, parent, false))


    override fun getItemCount(): Int = categoryImages.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(categoryImages[position])
    }
}