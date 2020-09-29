package com.example.helper.ui.calculator.my_database.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.helper.ui.calculator.my_database.model.Calculations;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CalculationsDAO {

    @Query("SELECT * FROM calc WHERE id=:calcId LIMIT 1")
    Flowable<Calculations> getCalculationsById (int calcId);

    @Query("SELECT * FROM calc")
    Flowable<List<Calculations>> getAllCalculations ();

    @Insert
    void insertCalculations (Calculations... calc);

    @Update
    void updateCalculations (Calculations... calc);

    @Delete
    void deleteCalculations (Calculations... calc);

    @Query("DELETE FROM calc")
    void deleteAllCalculations();
}
