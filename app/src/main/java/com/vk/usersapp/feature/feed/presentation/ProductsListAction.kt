package com.vk.usersapp.feature.feed.presentation

import com.vk.usersapp.feature.feed.model.Product

sealed interface ProductsListAction {
    data object Init : ProductsListAction
    data class QueryChanged(val query: String) : ProductsListAction
    data class UsersLoaded(val products: List<Product>) : ProductsListAction
    data class LoadError(val error: String) : ProductsListAction
    data object LoadMore : ProductsListAction
}