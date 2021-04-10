package by.kiparo.news.retrofit

import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolder {

    @GET("news")
    fun getPostAsync(): Deferred<JsonObject>
}