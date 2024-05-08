package com.vk.usersapp.feature.feed.model

data class ProductsResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)