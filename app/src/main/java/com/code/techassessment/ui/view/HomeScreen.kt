@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.code.techassessment.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.code.data.models.response.product.Product
import com.code.techassessment.R
import com.code.techassessment.navigation.ProductDescription
import com.code.techassessment.ui.theme.TechAssessmentTheme
import com.code.techassessment.ui.view.components.ErrorView
import com.code.techassessment.ui.view.components.LoadingView
import com.code.techassessment.ui.view.components.ProductCard
import com.code.techassessment.viewmodel.HomeViewModel
import com.code.techassessment.viewmodel.SearchResultUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

/**
 * State hoisting composable for home screen
 *
 * @param viewModel ViewModel for home screen
 * @param navController NavController for navigation
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavController = rememberNavController()
) {
    val searchResultsState = viewModel.searchResultState.collectAsState()
    val searchQuery = remember { MutableStateFlow("") }

    // Since, we don't want to make unnecessary API calls, we debounce the search query
    LaunchedEffect(searchQuery) {
        searchQuery
            .debounce(1000.milliseconds)
            .distinctUntilChanged()
            .collect { query ->
                if (query.isNotEmpty()) {
                    viewModel.searchProduct(query)
                }
            }
    }

    HomeScreenContent(
        searchResultsState = searchResultsState.value,
        onSearch = { query ->
            searchQuery.value = query
        },
        onLoadMore = {
            viewModel.searchProduct(query = searchQuery.value, loadMore = true)
        },
        onProductSelected = { productId ->
            navController.navigate(
                ProductDescription(productId = productId)
            )
        }
    )
}

/**
 * Composable to display the home screen content
 *
 * @param searchResultsState State of search results
 * @param onSearch Callback to handle search query
 * @param onLoadMore Callback to load more products
 * @param onProductSelected Callback to handle product selection
 */
@Composable
fun HomeScreenContent(
    searchResultsState: SearchResultUiState,
    onSearch: (String) -> Unit,
    onLoadMore: () -> Unit,
    onProductSelected: (productId: String) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Text Field for user to input search query
            ProductSearchBar(
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    if (searchText.length >= 3) {
                        onSearch(it)
                    }
                })
        }

        // Display welcome message or product list based on search results
        if (searchText.isEmpty()) {
            WelcomeMessage()
        } else {
            ProductsList(
                onLoadMore = onLoadMore,
                searchResultsState = searchResultsState,
                onProductSelected = onProductSelected
            )
        }
    }
}

/**
 * Composable to display welcome message
 *
 * @param message message to display
 */
@Composable
fun WelcomeMessage(
    message: String = stringResource(R.string.welcome)
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Composable to Search bar to search products
 *
 * @param searchText Current search text
 * @param onSearchTextChange Callback to handle search text changes
 */
@Composable
fun ProductSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                shape = RoundedCornerShape(5.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ),
        singleLine = true,
        placeholder = {
            Text(
                stringResource(R.string.search_best_buy),
                modifier = Modifier.padding(start = 5.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onSearchTextChange("")
                    }
                )
            }
        },
        shape = RoundedCornerShape(5.dp)
    )
}

/**
 * Composable to display list of products
 *
 * @param onLoadMore Callback to load more products
 * @param searchResultsState State of search results
 * @param onProductSelected Callback to handle product selection
 */
@Composable
fun ProductsList(
    onLoadMore: () -> Unit,
    searchResultsState: SearchResultUiState,
    onProductSelected: (productId: String) -> Unit
) {
    when (searchResultsState) {
        is SearchResultUiState.Loading ->
            LoadingView(
                modifier = Modifier
                    .fillMaxSize()
            )

        is SearchResultUiState.Success -> {
            val products = searchResultsState.results
            if (products.isEmpty()) {
                NoProductsFound()
                return // No need to continue if there are no products
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                state = rememberLazyListState()
            ) {
                items(searchResultsState.results.size) { productIndex ->
                    val product = products[productIndex]
                    ProductCard(
                        imageUrl = product.highResImage,
                        name = product.name,
                        price = product.salePrice.toString(),
                        onProductSelected = {
                            onProductSelected(product.sku)
                        }
                    )

                    // Once user reach end of the page, we load next page
                    if (productIndex == products.lastIndex && !searchResultsState.endReached) {
                        onLoadMore()
                    }
                }

                // Show progress indicator when loading more items
                if (searchResultsState.isAppending)
                    item {
                        LoadingView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
            }
        }

        is SearchResultUiState.Error -> ErrorView(message = searchResultsState.message)
    }
}

/**
 * Composable to display when no products are found
 */
@Composable
fun NoProductsFound() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_products_found),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Preview of the [HomeScreenContent]
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TechAssessmentTheme {
        HomeScreenContent(
            searchResultsState = SearchResultUiState.Success(
                results = listOf(
                    Product(name = "Product 1"),
                    Product(name = "Product 2"),
                    Product(name = "Product 3")
                ),
                endReached = false,
                isAppending = false
            ),
            onSearch = {},
            onLoadMore = {},
            onProductSelected = {}
        )
    }
}
