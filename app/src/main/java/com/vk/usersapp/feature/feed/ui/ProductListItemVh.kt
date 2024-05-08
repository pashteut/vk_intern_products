package com.vk.usersapp.feature.feed.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vk.usersapp.databinding.ViewholderProductBinding
import com.vk.usersapp.feature.feed.model.Product

class ProductListItemVh(private val binding: ViewholderProductBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        binding.user = product
    }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}