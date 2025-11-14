package com.code.techassessment.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home : NavRoute

@Serializable
data class ProductDescription(val productId: String) : NavRoute