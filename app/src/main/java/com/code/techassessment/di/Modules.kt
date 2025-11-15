package com.code.techassessment.di

import com.code.techassessment.viewmodel.HomeViewModel
import com.code.techassessment.viewmodel.product.ProductDescriptionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for view models
 */
val viewModelModules = module {
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        ProductDescriptionViewModel(get())
    }
}