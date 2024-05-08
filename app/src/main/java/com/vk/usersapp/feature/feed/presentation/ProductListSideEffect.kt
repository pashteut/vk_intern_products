package com.vk.usersapp.feature.feed.presentation

sealed interface ProductListSideEffect {
    data class LoadUsers(val query: String) : ProductListSideEffect
}