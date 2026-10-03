package com.rafael.alugueldecarro.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://example.com/api/"

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                MockApiInterceptor()
            )
            .build()

    val apiService: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}