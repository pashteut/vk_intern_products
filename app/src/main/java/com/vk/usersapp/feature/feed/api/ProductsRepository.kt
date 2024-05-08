package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.core.Retrofit
import com.vk.usersapp.feature.feed.model.Product

class ProductsRepository {
    private val api: ProductsApi by lazy { Retrofit.getClient().create(ProductsApi::class.java) }

    suspend fun getProducts(limit:Int = 30, skip:Int = 0): List<Product> {
        return api.getProducts(
            limit = limit,
            skip = skip
        ).products
    }

    suspend fun getTotal(limit:Int = 30, skip:Int = 0): Int =
        api.getProducts(
            limit = limit,
            skip = skip
        ).total
}