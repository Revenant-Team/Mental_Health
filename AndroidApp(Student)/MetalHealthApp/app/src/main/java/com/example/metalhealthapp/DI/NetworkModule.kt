package com.example.metalhealthapp.DI

import com.example.metalhealthapp.API.Apis
import com.example.metalhealthapp.Repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // increase connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // increase read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // increase write timeout
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Apis {
        return Retrofit.Builder()
            .baseUrl("https://mental-health-wpt3.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Apis::class.java)
    }

    @Provides
    @Singleton
    fun provideRepo(apis: Apis): Repo {
        return Repo(apis)
    }
}
