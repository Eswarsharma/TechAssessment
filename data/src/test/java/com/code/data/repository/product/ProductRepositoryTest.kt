package com.code.data.repository.product

import com.code.data.datasources.remote.ProductService
import com.code.data.models.response.product.Product
import com.code.data.models.response.product.ProductDetails
import com.code.data.models.response.product.ProductList
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    // mock product api service
    private val mockApi = mockk<ProductService>()

    // initializing repository using mock
    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        repository = ProductRepository(mockApi)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // Successful path for search results with data
    @Test
    fun `searchProducts returns flow of products`() = runTest {
        val searchQuery = "Air Pods"
        val expectedProducts = ProductList(
            products = listOf(
                Product(name = "Air Pods Pro", salePrice = 199.99),
                Product(name = "Air Pods Max", salePrice = 249.99),
            )
        )

        coEvery {
            mockApi.searchProduct(query = searchQuery, page = 1, lang = "en")
        } returns expectedProducts

        val result = repository.searchProduct(searchQuery, page = 1).first()

        assertEquals(expectedProducts, result)
        coVerify(exactly = 1) {
            mockApi.searchProduct(
                query = searchQuery,
                page = 1,
                lang = "en"
            )
        }
    }

    // successful path for search results with empty list
    @Test
    fun `searchProducts returns flow of empty product list`() = runTest {
        val searchQuery = "test"
        val expectedProducts = ProductList(products = emptyList())

        coEvery {
            mockApi.searchProduct(query = searchQuery, page = 1, lang = "en")
        } returns expectedProducts

        val result = repository.searchProduct(searchQuery, page = 1).first()

        assertEquals(expectedProducts, result)
    }

    // successful response for product details
    @Test
    fun `getProductDetails returns flow of product details`() = runTest {
        val productId = "12345"
        val expectedProductDetails = ProductDetails(
            name = "Air Pods",
            salePrice = 120.80
        )

        coEvery {
            mockApi.getProductDetails(id = productId, lang = "en")
        } returns expectedProductDetails

        val result = repository.getProductDetails(id = productId).first()

        assertEquals(expectedProductDetails, result)

        coVerify(exactly = 1) {
            mockApi.getProductDetails(
                id = productId,
                lang = "en"
            )
        }
    }

    // failure path for search results
    @Test
    fun `searchProduct throws exception when API fails`() = runTest {
        val searchQuery = "Macbook"
        val errorResponse = Response.error<ProductList>(
            401,
            "UnAuthorized".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            mockApi.searchProduct(query = searchQuery, page = 1, lang = "en")
        } throws HttpException(errorResponse)

        val result = runCatching {
            repository.searchProduct(searchQuery, page = 1).first()
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }
}