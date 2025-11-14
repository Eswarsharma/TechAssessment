@file: OptIn(ExperimentalGlideComposeApi::class)

package com.code.techassessment.ui.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.code.techassessment.R
import com.code.techassessment.ui.theme.TechAssessmentTheme

@Composable
fun ProductCard(
) {

}

@Composable
fun ProductImage(
    imageUrl: String
) {
    GlideImage(
        model = imageUrl,
        modifier = Modifier.fillMaxSize(),
        contentDescription = "",
        loading = placeholder(
            R.drawable.no_image
        ),
        failure = placeholder(
            R.drawable.no_image
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    TechAssessmentTheme {
        ProductCard()
    }
}

