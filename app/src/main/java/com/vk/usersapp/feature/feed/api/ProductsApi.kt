package com.vk.usersapp.feature.feed.api

import com.vk.usersapp.feature.feed.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int, @Query("skip") skip: Int): ProductsResponse

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String, @Query("limit") limit: Int, @Query("skip") skip: Int): ProductsResponse
}