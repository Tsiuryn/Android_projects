package alex.ts.coronavirusapp.retrofit

import alex.ts.coronavirusapp.const.HEADER
import alex.ts.coronavirusapp.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface JsonPlaceHolderApi {
    @Headers(HEADER)
    @GET("covid-19/")
    fun getPost(): Deferred<Post>
}