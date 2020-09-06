package by.kiparo.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [News::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDAO(): NewsDAO

    companion object{
    var INSTANCE: NewsDatabase? = null

    fun getNewsDataBase (context: Context): NewsDatabase?{
        if (INSTANCE == null){
            synchronized(NewsDatabase::class.java){
                INSTANCE = Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "my_news").build()
            }
        }
        return INSTANCE
    }

    fun destroyDataBase(){
        INSTANCE == null
    }
}
}