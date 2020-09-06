package by.kiparo.news.repository


import android.content.Context
import android.util.Log
import android.widget.Toast
import by.kiparo.news.database.News
import by.kiparo.news.database.NewsDatabase
import by.kiparo.news.model.NewsEntity
import by.kiparo.news.retrofit.JsonPlaceHolder
import com.facebook.cache.common.CacheKey
import com.facebook.cache.common.SimpleCacheKey
import com.facebook.cache.common.WriterCallback
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException


class Repository(val context: Context) {
    val API_URL = "https://fake-api.kiparo.by/news/"
    private lateinit var jsonPlaceHolderApi: JsonPlaceHolder

    private fun startRetrofit() {
        Log.d("TAG", "getRetrofit")
        val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolder::class.java)
    }

     suspend fun getPost(): List<News> {
         startRetrofit()
         val list = mutableListOf<News>()
         val request = jsonPlaceHolderApi.getPostAsync()
         try {
             val json = request.await().toString()
             val myJson = JSONObject(json)
             if (myJson.toString().isNotEmpty()){
                 list.addAll(parseJson(myJson))
                 addListToDB(list)
             }
         } catch (e: IOException) {
             val listFromDB = getListFromDB()
             if (listFromDB.isNotEmpty()){
                 Log.d("TAG", "in List")
                 Toast.makeText(context, "No internet, news from the Database!", Toast.LENGTH_LONG).show()
                 list.addAll(listFromDB)
             }
             else{
                 Log.d("TAG", e.message)
                 Toast.makeText(context, "No internet, Database is empty!", Toast.LENGTH_LONG).show()
             }
                Log.d("TAG", "no Internet")
         }
        return list
    }

    private  fun  parseJson(jsonObject: JSONObject): ArrayList<News>{
            val array = ArrayList<News>()
            try {
                val resultArray = jsonObject.getJSONArray("results")
                for (i in 0 until resultArray.length()) {
                    val newsObject = resultArray.getJSONObject(i)
                    val newsEntity = NewsEntity(newsObject)
                    val news = getNews(newsEntity)
                    array.add(news)
                    cacheImage(news.imageUrl)
                }
            } catch (e: JSONException) { }
        return array
    }

    private fun cacheImage(url: String){
        try {
            val cacheKey: CacheKey = SimpleCacheKey(url)
            val stream = ByteArrayOutputStream()
            val byteArray: ByteArray = stream.toByteArray()
            Fresco.getImagePipelineFactory().mainFileCache.insert(cacheKey, WriterCallback {
                outputStream -> outputStream.write(byteArray) })
        } catch (cacheWriteException: IOException) {
        }


    }

    private suspend fun addListToDB(list: List<News>){
        try {
            NewsDatabase.getNewsDataBase(context)!!.getNewsDAO().deleteAllNews()
            NewsDatabase.getNewsDataBase(context)!!.getNewsDAO().addListNews(list)
        }catch (e: Exception){
            Toast.makeText(context, "DataBase is Empty", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun getListFromDB(): List<News>{
        return NewsDatabase.getNewsDataBase(context)!!.getNewsDAO().getAllNews()
    }

    private fun getNews(newsEntity: NewsEntity): News {
        val title = newsEntity.title
        var imageURL = ""
        if (newsEntity.mediaEntity.isNotEmpty()) {
            imageURL = newsEntity.mediaEntity[0].url
        }
        val  summary = newsEntity.summary
        val  storyUrl = newsEntity.articleUrl
        return  News(title, summary, imageURL, storyUrl)
    }

}