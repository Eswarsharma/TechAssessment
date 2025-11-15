package com.code.techassessment.navigation

import kotlinx.serialization.Serializable

/*
 * Represents a route for Home in the navigation graph.
 */
@Serializable
object Home : NavRoute

/*
 * Represents a route for ProductDescription in the navigation graph.
 *
 * @param productId The id of the product.
 */
@Serializable
data class ProductDescription(val productId: String) : NavRoute