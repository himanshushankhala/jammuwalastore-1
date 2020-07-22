package com.jammuwalastore.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jammuwalastore.R

class CategoryItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_item)

        val images = listOf<CategoryImage>(
            CategoryImage("Dry Fruits", R.drawable.fruit),
            CategoryImage("Exotics", R.drawable.fruit),
            CategoryImage("Fruits", R.drawable.fruit),
            CategoryImage("JWS", R.drawable.fruit),
            CategoryImage("Vegetables", R.drawable.fruit)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.imagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CategoryImageAdapter(this, images)
    }
}