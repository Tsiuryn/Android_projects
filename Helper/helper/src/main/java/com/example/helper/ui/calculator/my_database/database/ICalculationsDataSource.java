package com.example.helper.ui.calculator.my_database.database;

import androidx.room.Insert;
import androidx.room.Query;

import com.example.helper.ui.calculator.my_database.model.Calculations;

import java.util.List;

import io.reactivex.Flowable;

public interface ICalculationsDataSource {

    Flowable<Calculations> getCalculationsById (int calcId);

    Flowable<List<Calculations>> getAllCalculations ();

    void insertCalculations (Calculations... calc);

    void updateCalculations (Calculations... calc);

    void deleteCalculations (Calculations... calc);

    void deleteAllCalculations();
}
