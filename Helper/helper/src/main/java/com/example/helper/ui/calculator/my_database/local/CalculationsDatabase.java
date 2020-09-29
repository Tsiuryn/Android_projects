package com.example.helper.ui.calculator.my_database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.helper.ui.calculator.my_database.model.Calculations;

import static com.example.helper.ui.calculator.my_database.local.CalculationsDatabase.DATABASE_VERSION;

@Database(entities = Calculations.class, version = DATABASE_VERSION)
public abstract class CalculationsDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "DATABASE_ROOM";
    public static final int DATABASE_VERSION = 1;

    public abstract CalculationsDAO calculationsDAO();

    private static CalculationsDatabase mInstance;

    public static CalculationsDatabase getInstance(Context context){
        if (mInstance == null){
            mInstance = Room.databaseBuilder(context, CalculationsDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}
