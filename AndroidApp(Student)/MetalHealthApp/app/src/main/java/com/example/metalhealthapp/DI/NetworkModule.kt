package com.example.metalhealthapp.DI

import com.example.metalhealthapp.API.Apis
import com.example.metalhealthapp.Repo.Repo
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
            .baseUrl("https://mental-health-wpt3.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRepo(apis: Apis) : Repo{
        return Repo(apis)
    }
}