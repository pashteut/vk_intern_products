package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.Product

data class ProductListState(
    val query: String = "",
    val items: List<Product> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val total: Int = 0
)