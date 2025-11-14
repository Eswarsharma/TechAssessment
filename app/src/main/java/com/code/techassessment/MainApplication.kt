package com.code.techassessment

import android.app.Application
import com.code.data.di.networkModule
import com.code.data.di.repositoryModules
import com.code.techassessment.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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