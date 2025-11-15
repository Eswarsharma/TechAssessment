@file: OptIn(ExperimentalGlideComposeApi::class)

package com.code.techassessment.ui.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.code.techassessment.R

/**
 * Product Image Component
 *
 * @param modifier Modifier for the component
 * @param imageUrl Image URL to be loaded
 */
@Composable
fun ProductImage(
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    GlideImage(
        modifier = modifier,
        model = imageUrl,
        contentDescription = "",
        loading = placeholder(
            R.drawable.no_image
        ),
        failure = placeholder(
            R.drawable.no_image
        )
    )
}