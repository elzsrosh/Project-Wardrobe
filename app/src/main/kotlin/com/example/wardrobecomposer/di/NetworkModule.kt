package com.example.wardrobecomposer.di

import com.example.wardrobecomposer.api.ColorApiService
import com.example.wardrobecomposer.api.TheColorApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_COLOR_API = "http://colormind.io/"
    private const val BASE_URL_THE_COLOR_API = "https://www.thecolorapi.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @Named("colorApiRetrofit")
    fun provideColorApiRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_COLOR_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("theColorApiRetrofit")
    fun provideTheColorApiRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_THE_COLOR_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideColorApiService(@Named("colorApiRetrofit") retrofit: Retrofit): ColorApiService =
        retrofit.create(ColorApiService::class.java)

    @Provides
    @Singleton
    fun provideTheColorApiService(@Named("theColorApiRetrofit") retrofit: Retrofit): TheColorApiService =
        retrofit.create(TheColorApiService::class.java)
}
