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

    suspend fun getTotal(query: String, limit:Int = 30, skip:Int = 0): Int =
        if (query.isNotEmpty())
            api.searchProducts(
                query = query,
                limit = limit,
                skip=skip
            ).total
        else api.getProducts(
            limit = limit,
            skip = skip
        ).total

    suspend fun searchProducts(query: String, skip:Int = 0): List<Product> {
        return api.searchProducts(
            query = query,
            limit = 30,
            skip = skip
        ).products
    }
}