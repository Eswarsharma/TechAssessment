package com.code.techassessment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.code.techassessment.ui.view.HomeScreen
import com.code.techassessment.ui.view.product.ProductDescription

fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable<Home> {
        HomeScreen(
            navController = navController
        )
    }
    composable<ProductDescription> { backStackEntry ->
        val args = backStackEntry.toRoute<ProductDescription>()
        ProductDescription(
            navController = navController,
            productId = args.productId
        )
    }
}