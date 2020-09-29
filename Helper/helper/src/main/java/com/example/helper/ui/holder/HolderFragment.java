package com.example.helper.ui.holder;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helper.R;

public class HolderFragment extends Fragment implements View.OnClickListener {
    private EditText diameter, capacity, time, changeCapacity;
    private TextView width, speed, volume, changingTime;


    private double dDiameter, dCapacity, dTime, dChangeCapacity, dWidth;

    private CalculateHolder holder = new CalculateHolder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_holder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diameter = view.findViewById(R.id.h_diameter);
        diameter.setOnClickListener(this);
        capacity = view.findViewById(R.id.h_capacity);
        capacity.setOnClickListener(this);
        time = view.findViewById(R.id.h_time);
        time.setOnClickListener(this);
        changeCapacity = view.findViewById(R.id.h_change_capacity);
        changeCapacity.setOnClickListener(this);

        width = view.findViewById(R.id.h_width);
        speed = view.findViewById(R.id.h_speed);
        volume = view.findViewById(R.id.h_volume);
        changingTime = view.findViewById(R.id.h_time_change);

        diameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calculateWidth();
                    calculateSpeed();
                    calculateVolume();
                    calculateChangingTime();
                }catch (Exception e){}

            }
        });
        capacity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calculateWidth();
                    calculateSpeed();
                    calculateVolume();
                    calculateChangingTime();
                }catch (Exception e){}

            }
        });
        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calculateWidth();
                    calculateSpeed();
                    calculateVolume();
                    calculateChangingTime();
                }catch (Exception e){}

            }
        });
        changeCapacity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    changeScreen();
                    calculateWidth();
                    calculateSpeed();
                    calculateVolume();
                    calculateChangingTime();
                }catch (Exception e){}
            }
        });

    }

    private void changeScreen (){
        Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.h_diameter:
                diameter.setText("");
                break;
            case R.id.h_capacity:
                capacity.setText("");
                break;
            case R.id.h_time:
                time.setText("");
                break;
            case R.id.h_change_capacity:
                changeCapacity.setText("");
                break;
        }

    }

    private void fillTheFields(){
        if (!diameter.getText().toString().equals("")){
            dDiameter = Double.parseDouble(diameter.getText().toString());
        }
        if (!capacity.getText().toString().equals("")){
            dCapacity = Double.parseDouble(capacity.getText().toString());
        }
        if (!time.getText().toString().equals("")){
            dTime = Double.parseDouble(time.getText().toString());
        }
        if (!changeCapacity.getText().toString().equals("")){
            dChangeCapacity = Double.parseDouble(changeCapacity.getText().toString());
        }
    }

    private void calculateWidth (){
        fillTheFields();
       if ( dDiameter > 0 && dCapacity > 0 && dTime > 0){
           dWidth = holder.calculateWidth(dCapacity, dTime, dDiameter);
           width.setText(String.format("%.3f", dWidth));
       }
    }

    private void calculateSpeed (){
        fillTheFields();
        if ( dDiameter > 0 && dCapacity > 0 && dTime > 0){
            double dSpeed = holder.calculateSpeed(dWidth, dTime);
            speed.setText(String.format("%.3f", dSpeed));
        }
    }

    private void calculateVolume (){
        fillTheFields();
        if ( dDiameter > 0 && dCapacity > 0 && dTime > 0){
            double dVolume = holder.calculateVolume(dDiameter, dWidth);
            volume.setText(String.format("%.3f", dVolume));
        }
    }

    private void calculateChangingTime (){
        fillTheFields();
        if (dDiameter > 0 && dCapacity > 0 && dTime > 0 && dChangeCapacity > 0){
            double dChangingTime = holder.calculateChangingTime(dWidth, dDiameter, dChangeCapacity);
            changingTime.setText(String.format("%.3f", dChangingTime));
        }
    }


}
