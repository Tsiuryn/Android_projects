package com.ts.alex.data.retrofit

import com.ts.alex.data.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers

interface RestApi {
    @Headers(HEADER)
    @GET("covid-19/")
    fun getPost(): Deferred<Post>
}