package com.code.data.models.response.product

import com.google.gson.annotations.SerializedName

data class ProductList(
    @SerializedName("Brand")
    val brand: String = "",
    val currentPage: Int = 0,
    val hasBrandStore: Boolean = false,
    val lastSearchDate: String = "",
    val pageSize: Int = 0,
    val products: List<Product> = emptyList(),
    val queryId: String = "",
    val total: Int = 0,
    val totalPages: Int = 0
)