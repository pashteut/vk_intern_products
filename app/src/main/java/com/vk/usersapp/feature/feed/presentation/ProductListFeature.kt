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

// MVI:
//         Action                 patch, state                  newState                            viewState
// View ------------> Feature -----------------> reducer ------------------------> Feature --------------------------> view
//          ^            |
//          |            |
//          | Action     |  sideEffect
//          |            |
//          |            v
//          |-------- Feature

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
                val users = withContext(Dispatchers.IO) {
                    productsRepository.getProducts(skip=state.loadedCount)
                }
                state = state.copy(loadedCount = state.loadedCount + users.size)
                submitAction(ProductsListAction.UsersLoaded(users))
            } catch (e: Exception) {
                Log.e("DIMAA", e.toString())
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
                val users = withContext(Dispatchers.IO) {
                    productsRepository.getProducts()
                }
                state = state.copy(total = productsRepository.getTotal(), loadedCount = users.size)
                submitAction(ProductsListAction.UsersLoaded(users))
            } catch (e: Exception) {
                Log.e("DIMAA", e.toString())
                submitAction(ProductsListAction.LoadError(e.message ?: "FATAL"))
            }
        }
    }

    fun isAllUsersLoaded(): Boolean {
        return state.loadedCount >= state.total
    }
}