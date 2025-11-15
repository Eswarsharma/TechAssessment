package com.code.techassessment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.code.techassessment.ui.view.HomeScreen
import com.code.techassessment.ui.view.product.ProductDescription

/**
 * Extension function to add the app navigation graph to the nav graph builder
 *
 * @param navController The nav controller
 */
fun NavGraphBuilder.appNavGraph(navController: NavController) {
    // Home screen
    composable<Home> {
        HomeScreen(
            navController = navController
        )
    }
    // Product description screen
    composable<ProductDescription> { backStackEntry ->
        val args = backStackEntry.toRoute<ProductDescription>()
        ProductDescription(
            navController = navController,
            productId = args.productId
        )
    }
}