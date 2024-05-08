package com.vk.usersapp.feature.feed.presentation

class ProductListReducer {
    fun applyAction(action: ProductsListAction, state: ProductListState): ProductListState {
        return when (action) {
            ProductsListAction.Init -> state.copy(isLoading = true)
            is ProductsListAction.QueryChanged -> state.copy(query = action.query, isLoading = true)
            is ProductsListAction.UsersLoaded -> state.copy(items = action.products, isLoading = false)
            is ProductsListAction.LoadError -> state.copy(error = action.error, isLoading = false)
            ProductsListAction.LoadMore -> state.copy(isLoading = true)
        }
    }
}