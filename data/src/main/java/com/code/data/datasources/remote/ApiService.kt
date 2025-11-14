package com.code.data.datasources.remote

import com.code.data.models.response.product.ProductList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchProduct(
        @Query("lang") lang: String,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): ProductList

    @GET("product/{id}")
    suspend fun getProductDetails(
        @Path("id") id: String,
        @Query("lang") lang: String
    )
}