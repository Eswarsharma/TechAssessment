package com.code.data.repository.product

import com.code.data.datasources.remote.ApiService
import java.util.Locale

class ProductRepository(
    private val apiService: ApiService
) {
    val currentLanguage: String = Locale.getDefault().language

    suspend fun searchProduct(query: String, page: Int) = apiService.searchProduct(
        lang = currentLanguage,
        query = query,
        page = page
    )

    suspend fun getProductDetails(id: String) = apiService.getProductDetails(
        id = id,
        lang = currentLanguage
    )
}