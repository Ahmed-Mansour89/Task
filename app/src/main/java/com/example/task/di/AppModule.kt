package com.example.task.di

import android.content.Context
import com.example.task.data.api.ApiService
import com.example.task.data.repository.WeatherRepositoryImpl
import com.example.task.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        // Create a logging interceptor
        val logging = HttpLoggingInterceptor().apply {
            // Set the desired log level (BODY, HEADERS, etc.)
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Build the OkHttpClient with the interceptor
        return OkHttpClient.Builder()
            .addInterceptor(logging) // Add the logging interceptor here
            .build()
    }
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService = Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)


    @Provides
    fun provideWeatherRepository(apiService: ApiService): WeatherRepository {
        return WeatherRepositoryImpl(apiService)
    }
}

