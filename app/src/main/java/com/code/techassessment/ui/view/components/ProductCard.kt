@file: OptIn(ExperimentalGlideComposeApi::class)

package com.code.techassessment.ui.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.code.techassessment.R
import com.code.techassessment.ui.theme.TechAssessmentTheme

/**
 * Reusable Product Card Component
 *
 * @param imageUrl Image URL for the product
 * @param name Name of the product
 * @param price Price of the product
 * @param onProductSelected Callback to handle product selection
 */
@Composable
fun ProductCard(
    imageUrl: String?,
    name: String,
    price: String,
    onProductSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .clickable(onClick = onProductSelected),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxSize()
                    .padding(horizontal = 5.dp, vertical = 10.dp),
                imageUrl = imageUrl
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryFixed,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$ $price",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

/**
 * Product Card Preview
 */
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    TechAssessmentTheme {
        ProductCard(
            imageUrl = "",
            name = "Product Name",
            price = "100",
            onProductSelected = {}
        )
    }
}

