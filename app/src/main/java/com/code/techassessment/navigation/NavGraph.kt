package com.code.techassessment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.code.techassessment.ui.view.HomeScreen
import com.code.techassessment.ui.view.product.ProductDescription

fun NavGraphBuilder.appNavGraph() {
    composable<Home> {
        HomeScreen()
    }
    composable<ProductDescription> { backStackEntry ->
        val args = backStackEntry.toRoute<ProductDescription>()
        ProductDescription(args.productId)
    }
}