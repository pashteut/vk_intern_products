package com.vk.usersapp

import com.vk.usersapp.feature.feed.api.ProductsRepository
import com.vk.usersapp.feature.feed.model.Product
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductsRepositoryTest {

//    private lateinit var productsRepository: ProductsRepository
//
//    @Before
//    fun setup() {
//        productsRepository = ProductsRepository()
//    }
//
//    @Test
//    fun `getUsers returns expected users`() = runBlocking {
//        val expectedProducts = listOf(Product("Terry", "Medhurst", "https://robohash.org/Terry.png?set=set4", "Capitol University", 50))
//        val actualUsers = productsRepository.getProducts(1, 0)
//        assertEquals(expectedProducts, actualUsers)
//    }
//
//    @Test
//    fun `getUsers returns expected users2`() = runBlocking {
//        val expectedProducts = listOf(Product("Tevin", "Prohaska", "https://robohash.org/Tevin.png?set=set4", "Gotland University College", 34))
//        val actualUsers = productsRepository.getProducts(1, 99)
//        assertEquals(expectedProducts, actualUsers)
//    }
//
//    @Test
//    fun `getTotal returns expected total`() = runBlocking {
//        val expectedTotal = 100
//        val actualTotal = productsRepository.getTotal("")
//        assertEquals(expectedTotal, actualTotal)
//    }
//
//    @Test
//    fun `getTotal returns expected total with query`() = runBlocking {
//        val expectedTotal = 2
//        val actualTotal = productsRepository.getTotal("terry")
//        assertEquals(expectedTotal, actualTotal)
//    }
//
//    @Test
//    fun `searchUsers returns expected users`() = runBlocking {
//        val expectedProducts = listOf(Product("Terry", "Medhurst", "https://robohash.org/Terry.png?set=set4", "Capitol University", 50), Product("Kody", "Terry", "https://robohash.org/Kody.png?set=set4", "Science University of Tokyo", 28))
//        val actualUsers = productsRepository.searchUsers("terry")
//        assertEquals(expectedProducts, actualUsers)
//    }
}