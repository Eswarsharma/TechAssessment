package com.code.techassessment.di

import com.code.techassessment.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel {
        HomeViewModel(get())
    }
}