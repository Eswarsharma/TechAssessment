package com.code.techassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.data.models.response.product.Product
import com.code.data.repository.product.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class SearchResultState {
    object Loading : SearchResultState()
    data class Success(
        val results: List<Product>,
        val isAppending: Boolean = false,
        val endReached: Boolean = false
    ) : SearchResultState()
    data class Error(val message: String) : SearchResultState()
}

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var currentPage = 1

    private val _searchResultState = MutableStateFlow<SearchResultState>(SearchResultState.Loading)
    val searchResultState = _searchResultState

    /**
     * Search for products based on the provided query.
     */
    fun searchProduct(query: String, loadMore: Boolean = false) {
        viewModelScope.launch {
            val currentState = _searchResultState.value

            // User searching for the first time
            if (!loadMore) {
                currentPage = 1
                _searchResultState.value = SearchResultState.Loading
            }

            // loading more items as user scrolls
            if (loadMore && currentState is SearchResultState.Success) {
                if (currentState.isAppending || currentState.endReached) return@launch
                _searchResultState.value = currentState.copy(isAppending = true)
                currentPage++
            }

            try {
                val response = productRepository.searchProduct(query = query, page = currentPage)
                if (!loadMore) {
                    // First page result
                    _searchResultState.value = SearchResultState.Success(
                        results = response.products,
                        endReached = response.products.isEmpty()
                    )
                } else if (currentState is SearchResultState.Success) {
                    // Append to existing list
                    _searchResultState.value = currentState.copy(
                        results = currentState.results + response.products,
                        isAppending = false,
                        endReached = response.products.isEmpty()
                    )
                }

            } catch (e: Exception) {
                _searchResultState.value = SearchResultState.Error(e.message ?: "Unknown error")
            }
        }
    }
}