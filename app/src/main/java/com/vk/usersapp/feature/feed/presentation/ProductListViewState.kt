package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.Product

sealed class ProductListViewState {
    data object Loading : ProductListViewState()
    data class Error(val errorText: String) : ProductListViewState()
    data class List(val itemsList: kotlin.collections.List<Product>) : ProductListViewState()
}