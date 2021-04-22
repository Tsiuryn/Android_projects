package com.ts.alex.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL = "http://api.openweathermap.org/"

fun providePlaceHolderApi(): RestApi {
    // HttpLoggingInterceptor - для отображения в логов
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    val retrofit = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(CoroutineCallAdapterFactory())
        client(client)
        baseUrl(URL)
    }.build()

    return retrofit.create(RestApi::class.java)
}