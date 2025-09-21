package com.example.metalhealthapp.DI

import com.example.metalhealthapp.API.Apis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit () : Apis{
        return Retrofit.Builder()
            .baseUrl("https://speedyserve-server.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}