package by.kiparo.news.database

import androidx.room.*

@Dao
abstract class NewsDAO {

//    @Transaction
//    fun addListWithTransaction (list: List<News>){
//        deleteAllNews()
//        addListNews(list)
//    }

    @Insert
    abstract suspend fun addNews(news: News)

    @Insert
    abstract suspend fun addListNews(list: List<News>)

    @Update
    abstract suspend fun updateNews(news: News)

    @Delete
    abstract suspend fun deleteNews(news: News)

    @Query("SELECT * FROM my_news")
    abstract suspend fun getAllNews(): List<News>

    @Query ("DELETE FROM my_news")
    abstract suspend fun deleteAllNews()

    @Query("SELECT * FROM my_news WHERE id ==:id")
    abstract suspend fun getNews(id: Int): News

}