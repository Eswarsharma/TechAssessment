package com.code.data.di

import com.code.data.BuildConfig
import com.code.data.datasources.remote.ProductService
import com.code.data.repository.product.ProductRepository
import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Koin module for network operations
 */
val networkModule = module {
    // OKHttp
    single {
        OkHttpClient.Builder()
            .connectionPool(
                ConnectionPool(
                    5,
                    120,
                    TimeUnit.SECONDS
                )
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", Locale.getDefault().language)
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                "Chrome/120.0.0.0 Safari/537.36"
                    )
                    .build()
                chain.proceed(request)
            })
            .build()
    }

    // Gson
    single {
        GsonBuilder()
            .create()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // ApiService
    single {
        get<Retrofit>().create(ProductService::class.java)
    }
}

/**
 * Koin module for repositories
 */
val repositoryModules = module {
    single {
        ProductRepository(get())
    }
}