package com.code.techassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.data.models.response.product.Product
import com.code.data.repository.product.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Represents the state of the search results.
 */
sealed class SearchResultUiState {
    object Loading : SearchResultUiState()
    data class Success(
        val results: List<Product>,
        val isAppending: Boolean = false,
        val endReached: Boolean = false
    ) : SearchResultUiState()

    data class Error(val message: String) : SearchResultUiState()
}

/**
 * View model class for the home screen.
 *
 * @param productRepository The product repository.
 *
 * NOTE: If we have any business logic involved, we inject use case class instead of repository
 */
class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Current page number for pagination
    private var currentPage = 1

    // State flow to hold the search results ui state
    private val _searchResultUiState =
        MutableStateFlow<SearchResultUiState>(SearchResultUiState.Loading)
    val searchResultState = _searchResultUiState

    /**
     * Search for products based on the provided query.
     */
    fun searchProduct(query: String, loadMore: Boolean = false) {
        viewModelScope.launch {
            val currentState = _searchResultUiState.value

            // User searching for the first time
            if (!loadMore) {
                currentPage = 1
                _searchResultUiState.value = SearchResultUiState.Loading
            }

            // loading more items as user scrolls
            if (loadMore && currentState is SearchResultUiState.Success) {
                if (currentState.isAppending || currentState.endReached) return@launch
                _searchResultUiState.value = currentState.copy(isAppending = true)
                currentPage++
            }

            productRepository.searchProduct(query = query, page = currentPage)
                .catch {
                    _searchResultUiState.value =
                        SearchResultUiState.Error(it.message ?: "Unknown error")
                }
                .collect { response ->
                    if (!loadMore) {
                        // First page result
                        _searchResultUiState.value = SearchResultUiState.Success(
                            results = response.products,
                            endReached = response.products.isEmpty()
                        )
                    } else if (currentState is SearchResultUiState.Success) {
                        // Append to existing list
                        _searchResultUiState.value = currentState.copy(
                            results = currentState.results + response.products,
                            isAppending = false,
                            endReached = response.products.isEmpty()
                        )
                    }
                }
        }
    }
}