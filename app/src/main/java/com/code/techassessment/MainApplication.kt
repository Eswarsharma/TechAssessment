package com.code.techassessment

import android.app.Application
import com.code.data.di.networkModule
import com.code.data.di.repositoryModules
import com.code.techassessment.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// Custom Application class to initialize Koin and other dependencies
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin with the required modules
        startKoin {
            androidContext(this@MainApplication)
            modules(
                viewModelModules,
                networkModule,
                repositoryModules
            )
        }
    }
}