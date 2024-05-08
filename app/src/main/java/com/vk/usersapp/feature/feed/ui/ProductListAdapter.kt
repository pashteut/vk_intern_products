package com.vk.usersapp.feature.feed.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.vk.usersapp.databinding.ViewholderProductBinding
import com.vk.usersapp.feature.feed.model.Product

class ProductListAdapter : Adapter<ProductListItemVh>() {

    private val dataset: MutableList<Product> = mutableListOf()

    fun setUsers(products: List<Product>) {
        dataset.clear()
        dataset.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListItemVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewholderProductBinding.inflate(inflater, parent, false)
        return ProductListItemVh(binding)
    }

    override fun onBindViewHolder(holder: ProductListItemVh, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size
}