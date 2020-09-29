package com.example.helper.ui.calculator.my_database.local;

import android.util.Log;

import com.example.helper.ui.calculator.my_database.database.ICalculationsDataSource;
import com.example.helper.ui.calculator.my_database.model.Calculations;

import java.util.List;

import io.reactivex.Flowable;

public class CalculationsDataSource implements ICalculationsDataSource {
    private CalculationsDAO calculationsDAO;
    private static CalculationsDataSource mInstance;

    public CalculationsDataSource (CalculationsDAO calculationsDAO){
        this.calculationsDAO = calculationsDAO;
    }

    public static CalculationsDataSource getInstance(CalculationsDAO calculationsDAO){
        if (mInstance == null){
            mInstance = new CalculationsDataSource(calculationsDAO);
        }
        return mInstance;
    }
    @Override
    public Flowable<Calculations> getCalculationsById(int calcId) {
        return calculationsDAO.getCalculationsById(calcId);
    }

    @Override
    public Flowable<List<Calculations>> getAllCalculations() {
        return calculationsDAO.getAllCalculations();
    }

    @Override
    public void insertCalculations(Calculations... calc) {
        calculationsDAO.insertCalculations(calc);
    }

    @Override
    public void updateCalculations(Calculations... calc) {
        calculationsDAO.updateCalculations(calc);
    }

    @Override
    public void deleteCalculations(Calculations... calc) {
        calculationsDAO.deleteCalculations(calc);
    }

    @Override
    public void deleteAllCalculations() {
        calculationsDAO.deleteAllCalculations();
    }
}
