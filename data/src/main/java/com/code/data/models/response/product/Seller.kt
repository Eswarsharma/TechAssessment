package com.code.data.models.response.product

/**
 * data class to hold seller information
 */
data class Seller(
    val canSell: Boolean = false,
    val id: String = "",
    val isPremierSeller: Boolean = false,
    val name: String = ""
)