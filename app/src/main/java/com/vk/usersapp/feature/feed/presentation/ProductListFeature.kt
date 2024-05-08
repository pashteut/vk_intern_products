package com.vk.usersapp.feature.feed.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.usersapp.core.MVIFeature
import com.vk.usersapp.feature.feed.api.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListFeature : MVIFeature, ViewModel() {
    private val mutableViewStateFlow = MutableStateFlow<ProductListViewState>(ProductListViewState.Loading)
    val viewStateFlow: StateFlow<ProductListViewState> = mutableViewStateFlow.asStateFlow()

    private var state: ProductListState = ProductListState()

    private val reducer = ProductListReducer()
    private val productsRepository = ProductsRepository()
    val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query.collect {
                submitAction(ProductsListAction.QueryChanged(it))
            }
        }
    }

    fun submitAction(action: ProductsListAction) {
        state = reducer.applyAction(action, state)

        val viewState = createViewState(state)
        mutableViewStateFlow.value = viewState

        when (action) {
            ProductsListAction.Init,
            is ProductsListAction.QueryChanged -> {submitSideEffect(ProductListSideEffect.LoadUsers(state.query))}
            is ProductsListAction.UsersLoaded -> state = state.copy(isLoadingMore = false)
            is ProductsListAction.LoadError -> Unit
            ProductsListAction.LoadMore -> if(!state.isLoadingMore)loadMoreUsers()
        }
    }

    private fun loadMoreUsers() {
        state = state.copy(isLoadingMore = true)
        viewModelScope.launch {
            try {
                val products = withContext(Dispatchers.IO) {
                    if (state.query.isBlank()) {
                        productsRepository.getProducts(skip=state.items.size)
                    } else {
                        productsRepository.searchProducts(state.query, skip=state.items.size)
                    }
                }
                submitAction(ProductsListAction.UsersLoaded(state.items + products))
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                submitAction(ProductsListAction.LoadError(e.message ?: "FATAL"))
            }
        }
    }

    private fun createViewState(state: ProductListState): ProductListViewState {
        return when {
            state.isLoading -> ProductListViewState.Loading
            !state.error.isNullOrBlank() -> ProductListViewState.Error(state.error)
            else -> ProductListViewState.List(state.items)
        }
    }

    private fun submitSideEffect(sideEffect: ProductListSideEffect) {
        when (sideEffect) {
            is ProductListSideEffect.LoadUsers -> loadUsers(sideEffect.query)
        }
    }

    private fun loadUsers(query: String) {
        viewModelScope.launch {
            try {
                val products = withContext(Dispatchers.IO) {
                    if (state.query.isBlank()) {
                        productsRepository.getProducts()
                    } else {
                        productsRepository.searchProducts(state.query)
                    }
                }
                state = state.copy(total = productsRepository.getTotal(query))
                submitAction(ProductsListAction.UsersLoaded(products))
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                submitAction(ProductsListAction.LoadError(e.message ?: "FATAL"))
            }
        }
    }

    fun isAllUsersLoaded(): Boolean {
        return state.items.size >= state.total
    }
}