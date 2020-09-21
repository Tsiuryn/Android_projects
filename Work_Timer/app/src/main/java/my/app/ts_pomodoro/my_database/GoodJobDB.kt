package my.app.ts_pomodoro.my_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GoodJobEntity::class], version = 1)
abstract class GoodJobDB : RoomDatabase() {
    abstract fun getGoodJobDAO(): GoodJobDAO

    companion object {
        private var INSTANCE: GoodJobDB? = null

        fun getGoodJobDB(context: Context): GoodJobDB? {
            if (INSTANCE == null) {
                synchronized(GoodJobDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GoodJobDB::class.java, "job_timer"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE == null
        }
    }
}