@file:OptIn(ExperimentalMaterial3Api::class)

package com.code.techassessment.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.code.techassessment.R
import com.code.techassessment.ui.theme.TechAssessmentTheme
import com.code.techassessment.viewmodel.HomeViewModel
import com.code.techassessment.viewmodel.SearchResultState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val searchState = viewModel.searchResultState.collectAsState()
    HomeScreenContent(
        searchState = searchState.value,
        onSearch = { query ->
            viewModel.searchProduct(query)
        },
        onLoadMore = {
            viewModel.searchProduct(query = "", loadMore = true)
        },
        onProductSelected = {

        }
    )
}

@Composable
fun HomeScreenContent(
    searchState: SearchResultState,
    onSearch: (String) -> Unit,
    onLoadMore: () -> Unit,
    onProductSelected: (productId: String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.BottomCenter
            ) {
                ProductSearchBar(
                    modifier = Modifier
                        .padding(
                            bottom = 15.dp
                        )
                        .background(
                            shape = RoundedCornerShape(2.dp),
                            color = Color.Transparent
                        ),
                    textFieldState = TextFieldState(),
                    onSearch = onSearch,
                    onLoadMore = onLoadMore,
                    searchState = searchState
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.welcome),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )
}

@Composable
fun ProductSearchBar(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onLoadMore: () -> Unit,
    searchState: SearchResultState
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                modifier = Modifier.background(
                    shape = RoundedCornerShape(2.dp),
                    color = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onQueryChange = {
                    textFieldState.edit {
                        replace(0, length, it)
                        onSearch(textFieldState.text.toString())
                    }
                },
                onSearch = {
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text(
                        stringResource(R.string.search_best_buy),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (searchState) {
                is SearchResultState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchResultState.Success -> {
                    LazyColumn {
                        items(searchState.results) { product ->
                            ListItem(
                                headlineContent = { Text(product.name) },
                                modifier = Modifier
                                    .clickable {
                                        textFieldState.edit { replace(0, length, product.name) }
                                        expanded = false
                                    }
                                    .fillMaxWidth()
                            )
                        }

                        if (searchState.isAppending) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }

                    // Trigger load more when scrolling hits bottom
                    LaunchedEffect(searchState.results) {
                        if (!searchState.endReached && !searchState.isAppending) {
                            onLoadMore()
                        }
                    }
                }

                is SearchResultState.Error -> {
                    Text("Error: ${searchState.message}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TechAssessmentTheme {
        HomeScreenContent(
            searchState = SearchResultState.Loading,
            onSearch = {},
            onLoadMore = {},
            onProductSelected = {}
        )
    }
}
