package my.app.ts_pomodoro.my_database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "job_timer")
data class GoodJobEntity (
    val date: String,
    val finishTime: String,
    val workingTime: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}