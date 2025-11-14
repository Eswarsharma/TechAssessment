package com.code.techassessment.ui.view.product

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProductDescription(
    navController: NavController = rememberNavController(),
    productId: String
) {
    Text(
        text = "Product Description: $productId",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}