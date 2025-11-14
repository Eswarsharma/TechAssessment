package com.code.data.repository.product

import com.code.data.datasources.remote.ProductService
import java.util.Locale

class ProductRepository(
    private val productService: ProductService
) {
    val currentLanguage: String = Locale.getDefault().language

    suspend fun searchProduct(query: String, page: Int) = productService.searchProduct(
        lang = currentLanguage,
        query = query,
        page = page
    )

    suspend fun getProductDetails(id: String) = productService.getProductDetails(
        id = id,
        lang = currentLanguage
    )
}