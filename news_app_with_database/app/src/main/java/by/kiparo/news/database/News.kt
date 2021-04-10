package by.kiparo.news.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_news")
data class News(
        var title: String = "",
        var summary: String = "",
        val imageUrl: String = "",
        val storyUrl: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}