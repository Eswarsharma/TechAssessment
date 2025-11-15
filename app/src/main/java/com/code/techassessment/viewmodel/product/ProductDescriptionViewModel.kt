package com.code.techassessment.viewmodel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.data.models.response.product.ProductDetails
import com.code.data.repository.product.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Sealed class representing the ui state of the product details.
 */
sealed class ProductDetailsUiState {
    object Loading : ProductDetailsUiState()
    data class Success(val productDetails: ProductDetails) : ProductDetailsUiState()
    data class Error(val message: String) : ProductDetailsUiState()
}

/**
 * View model class for the product description screen.
 *
 * @param productRepository The product repository.
 *
 * NOTE: If we have any business logic involved, we inject use case class instead of repository
 */
class ProductDescriptionViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    // State flow to hold the product details ui state
    private val _productDetailState =
        MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val productDetailState = _productDetailState

    /**
     * Get product details based on the provided product id.
     *
     * @param productId The product id.
     *
     * @return Flow emitting the product details.
     */
    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            productRepository.getProductDetails(productId)
                .catch {
                    _productDetailState.value =
                        ProductDetailsUiState.Error(it.message ?: "Unknown error")
                }
                .collect {
                    _productDetailState.value = ProductDetailsUiState.Success(it)
                }
        }
    }
}