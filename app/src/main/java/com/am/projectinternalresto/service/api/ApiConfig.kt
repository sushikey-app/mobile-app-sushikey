package com.am.projectinternalresto.service.api

import com.am.projectinternalresto.utils.Key
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder().baseUrl(Key.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()

        return retrofit.create(ApiService::class.java)
    }
}