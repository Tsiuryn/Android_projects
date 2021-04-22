package com.ts.alex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1)
abstract class CityDB : RoomDatabase() {
    abstract fun cityDAO(): CityDAO

    companion object {
        private var INSTANCE: CityDB? = null

        fun getGoodJobDB(context: Context): CityDB? {
            if (INSTANCE == null) {
                synchronized(CityDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CityDB::class.java, "db_city"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE == null
        }
    }
}