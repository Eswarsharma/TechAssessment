package com.code.techassessment.ui.view.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.code.data.models.response.product.AdditionalMedia
import com.code.data.models.response.product.ProductDetails
import com.code.techassessment.R
import com.code.techassessment.ui.theme.TechAssessmentTheme
import com.code.techassessment.ui.view.components.ErrorView
import com.code.techassessment.ui.view.components.LoadingView
import com.code.techassessment.ui.view.components.ProductImage
import com.code.techassessment.viewmodel.product.ProductDescriptionViewModel
import com.code.techassessment.viewmodel.product.ProductDetailsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * State hoisting composable for product description screen
 *
 * @param viewModel ViewModel for product description screen
 * @param navController NavController for navigation
 * @param productId Id of the product to be displayed
 */
@Composable
fun ProductDescription(
    viewModel: ProductDescriptionViewModel = koinViewModel(),
    navController: NavController = rememberNavController(),
    productId: String
) {
    val uiState = viewModel.productDetailState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProductDetails(productId)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.BottomStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }
        },
        content = { innerPadding ->
            ProductDescriptionContent(
                modifier = Modifier
                    .padding(innerPadding),
                productDetailsState = uiState.value,
                onAddToCart = {}
            )
        }
    )
}

/**
 * Composable to display the product description
 *
 * @param modifier Modifier for the component
 * @param productDetailsState State of the product details
 * @param onAddToCart Callback to add the product to the cart
 */
@Composable
fun ProductDescriptionContent(
    modifier: Modifier = Modifier,
    productDetailsState: ProductDetailsUiState,
    onAddToCart: () -> Unit,
) {
    var showToast by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    when (productDetailsState) {
        is ProductDetailsUiState.Loading ->
            LoadingView(
                modifier = modifier
                    .fillMaxSize()
            )

        is ProductDetailsUiState.Success -> {
            val product = productDetailsState.productDetails
            val scrollState = rememberScrollState()
            var showAddToCart by remember { mutableStateOf(false) }

            // Animate the visibility of the add to cart button when the scroll state changes
            LaunchedEffect(scrollState.isScrollInProgress) {
                if (scrollState.isScrollInProgress)
                    showAddToCart = false
                else {
                    delay(700)
                    showAddToCart = true
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Product Images
                    ImageSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        images = product.productImages.map { it.url })

                    // Product name
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product price
                    Text(
                        text = "$ ${product.salePrice}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product description
                    Text(
                        text = buildAnnotatedString {
                            append(
                                HtmlCompat.fromHtml(
                                    product.longDescription,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            )
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                // Add to cart
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = showAddToCart,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    Button(
                        onClick = {
                            onAddToCart()
                            scope.launch {
                                showToast = true
                                delay(1000)
                                showToast = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_to_cart),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Custom toast
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(bottom = 80.dp)
                        .align(Alignment.BottomCenter),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                    visible = showToast
                ) {
                    CustomToast()
                }
            }
        }

        is ProductDetailsUiState.Error -> ErrorView(message = productDetailsState.message)
    }
}

/**
 * Composable to display the toast message
 *
 * @param message Message to be displayed
 */
@Composable
fun CustomToast(
    message: String = stringResource(R.string.added_to_cart)
) {
    Text(
        modifier = Modifier
            .size(width = 150.dp, height = 40.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .wrapContentHeight(Alignment.CenterVertically),
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = Color.White
    )
}

/**
 * Composable to display the image slider for the product
 *
 * @param modifier Modifier for the component
 * @param images List of images to be displayed
 */
@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    images: List<String>
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        ProductImage(
            modifier = Modifier
                .fillMaxSize(),
            imageUrl = images[page]
        )
    }
    if (images.size > 1)
        PageIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
}

/**
 * Composable to display the page indicator for the image slider
 *
 * @param pagerState State of the pager
 * @param modifier Modifier for the component
 * @param activeColor Color of the active page indicator
 * @param inactiveColor Color of the inactive page indicator
 * @param indicatorSize Size of the page indicator
 * @param spacing Spacing between the page indicators
 */
@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.LightGray,
    indicatorSize: Dp = 10.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color by animateColorAsState(
                targetValue = if (pagerState.currentPage == iteration) activeColor else inactiveColor,
                animationSpec = tween(durationMillis = 300), label = "indicator_color"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/**
 * Preview of the [ProductDescriptionContent]
 */
@Preview(showBackground = true)
@Composable
fun ProductDescriptionPreview() {
    TechAssessmentTheme {
        ProductDescriptionContent(
            productDetailsState = ProductDetailsUiState.Success(
                productDetails = ProductDetails(
                    name = "Product 1",
                    salePrice = 100.0,
                    regularPrice = 150.0,
                    longDescription = "This is a product description",
                    productImages = listOf(
                        AdditionalMedia(url = "https://picsum.photos/200/300"),
                        AdditionalMedia(url = "https://picsum.photos/200/300"),
                        AdditionalMedia(url = "https://picsum.photos/200/300")
                    )
                )
            ),
            onAddToCart = {}
        )
    }
}