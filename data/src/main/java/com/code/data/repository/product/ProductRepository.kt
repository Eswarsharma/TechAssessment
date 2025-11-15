package com.code.data.repository.product

import com.code.data.datasources.remote.ProductService
import kotlinx.coroutines.flow.flow
import java.util.Locale

/**
 * Repository class for product related operations
 *
 * Eventually, if we decide to have local storage, we inject DataSource instead of service, and end up having localDataSource, and RemoteDataSource, like below
 * class ProductRepository(
 *    private val localDataSource: ProductLocalDataSource,
 *    private val remoteDataSource: ProductRemoteDataSource
 * ){}
 */
class ProductRepository(
    private val productService: ProductService
) {
    val currentLanguage: String = Locale.getDefault().language

    /**
     * Search for products based on the provided query and page number.
     *
     * @param query The search query.
     * @param page The page number for pagination.
     *
     * @return Flow emitting the search results.
     */
    fun searchProduct(query: String, page: Int) = flow {
        emit(
            productService.searchProduct(
                lang = currentLanguage,
                query = query,
                page = page
            )
        )
    }

    /**
     * Get product details based on the provided product id.
     *
     * @param id The product id.
     *
     * @return Flow emitting the product details.
     */
    fun getProductDetails(id: String) = flow {
        emit(
            productService.getProductDetails(
                id = id,
                lang = currentLanguage
            )
        )
    }
}