package com.example.helper.ui.calculator.my_database.database;

import com.example.helper.ui.calculator.my_database.model.Calculations;

import java.util.List;

import io.reactivex.Flowable;

public class CalculationsRepository implements ICalculationsDataSource {
    private ICalculationsDataSource mLocalDataSource;
    private static CalculationsRepository mInstance;

    private CalculationsRepository (ICalculationsDataSource mLocalDataSource){
        this.mLocalDataSource = mLocalDataSource;
    }

    public static CalculationsRepository getInstance(ICalculationsDataSource mLocalDataSource){
        if (mInstance == null){
            mInstance = new CalculationsRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<Calculations> getCalculationsById(int calcId) {
        return mLocalDataSource.getCalculationsById(calcId);
    }

    @Override
    public Flowable<List<Calculations>> getAllCalculations() {
        return mLocalDataSource.getAllCalculations();
    }

    @Override
    public void insertCalculations(Calculations... calc) {
        mLocalDataSource.insertCalculations(calc);
    }

    @Override
    public void updateCalculations(Calculations... calc) {
        mLocalDataSource.updateCalculations(calc);
    }

    @Override
    public void deleteCalculations(Calculations... calc) {
        mLocalDataSource.deleteCalculations(calc);
    }

    @Override
    public void deleteAllCalculations() {
        mLocalDataSource.deleteAllCalculations();
    }
}
