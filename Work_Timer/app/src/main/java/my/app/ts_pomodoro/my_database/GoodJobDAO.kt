package my.app.ts_pomodoro.my_database

import androidx.room.*

@Dao
interface GoodJobDAO {

    @Insert
    suspend fun addGoodJob (goodJob: GoodJobEntity)

    @Update
    suspend fun updateGoodJob(goodJob: GoodJobEntity)

    @Delete
    suspend fun deleteGoodJob(goodJob: GoodJobEntity)

    @Query("SELECT * FROM job_timer")
    suspend fun getAllGoodJob(): List<GoodJobEntity>

    @Query("SELECT * FROM job_timer WHERE id ==:id LIMIT 1")
    suspend fun getGoodJob(id: Int): GoodJobEntity
}