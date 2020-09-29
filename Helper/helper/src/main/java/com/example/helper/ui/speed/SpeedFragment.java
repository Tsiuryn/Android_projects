package com.example.helper.ui.speed;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helper.R;

import java.util.ArrayList;

public class SpeedFragment extends Fragment implements View.OnClickListener {

    private EditText capacity;
    private EditText speed;
    private EditText diameter;

    private EditText cyl_diameter, cyl_height, cyl_volume;
    private RadioButton rad_cyl_diameter, rad_cyl_height, rad_cyl_volume;
    private RadioButton rCapacity;
    private RadioButton rSpeed;
    private RadioButton rDiameter;
    private CalculateSpeed calculate;
    private CalculateTube tube = new CalculateTube();

    private double cDiameter, cHeight, cVolume;

    private ArrayList<RadioButton> cyl_radio = new ArrayList<>();
    private ArrayList<EditText> cyl_text = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_speed, container, false);
        return root;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        if (menu.findItem(R.id.deleteBase) != null){
//            menu.findItem(R.id.deleteBase).setVisible(false);
//            menu.findItem(R.id.dataBase).setVisible(false);
//        }
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);
        capacity = view.findViewById(R.id.speed_capacity);
        speed = view.findViewById(R.id.speed_Speed);
        diameter = view.findViewById(R.id.speed_diametr);
        rCapacity = view.findViewById(R.id.radio_speed_capacity);
        rDiameter = view.findViewById(R.id.radio_speed_diametr);
        rSpeed = view.findViewById(R.id.radio_speed_Speed);
        rCapacity.setOnClickListener(this);
        rDiameter.setOnClickListener(this);
        rSpeed.setOnClickListener(this);
        capacity.setOnClickListener(this);
        speed.setOnClickListener(this);
        diameter.setOnClickListener(this);
        radioIsChecked(rCapacity);
        calculate = new CalculateSpeed();

        cyl_diameter = view.findViewById(R.id.cyl_diameter);
        cyl_height = view.findViewById(R.id.cyl_height);
        cyl_volume = view.findViewById(R.id.cyl_volume);
        cyl_diameter.setOnClickListener(this);
        cyl_height.setOnClickListener(this);
        cyl_volume.setOnClickListener(this);

        rad_cyl_diameter = view.findViewById(R.id.radio_cyl_diameter);
        rad_cyl_height = view.findViewById(R.id.radio_cyl_height);
        rad_cyl_volume = view.findViewById(R.id.radio_cyl_volume);
        rad_cyl_diameter.setOnClickListener(this);
        rad_cyl_height.setOnClickListener(this);
        rad_cyl_volume.setOnClickListener(this);
        fillArray();
        fillCylRadio(rad_cyl_diameter);

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
                    calculateDiameter();
                    calculateSpeed();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
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
                    calculateCapacity();
                    calculateSpeed();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        speed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    calculateDiameter();
                    calculateCapacity();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        cyl_diameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calcCylHeight();
                    calcCylVolume();
                } catch (Exception e) {

                }
            }
        });
        cyl_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calcCylDiameter();
                    calcCylVolume();
                } catch (Exception e) {

                }
            }
        });
        cyl_volume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calcCylDiameter();
                    calcCylHeight();
                } catch (Exception e) {

                }
            }
        });


    }



    private void fillArray() {
        cyl_radio.add(rad_cyl_diameter);
        cyl_radio.add(rad_cyl_height);
        cyl_radio.add(rad_cyl_volume);
        cyl_text.add(cyl_diameter);
        cyl_text.add(cyl_height);
        cyl_text.add(cyl_volume);
    }


    private void fillCylRadio(View view) {
        for (int i = 0; i < cyl_radio.size(); i++) {
            cyl_radio.get(i).setChecked(false);
        }
        for (int i = 0; i <cyl_radio.size() ; i++) {
            if (cyl_radio.get(i).equals(view)) {
                cyl_radio.get(i).setChecked(true);
                cyl_text.get(i).setInputType(InputType.TYPE_NULL);
                cyl_text.get(i).setTextIsSelectable(false);
            } else if (!cyl_radio.get(i).equals(view)) {
                cyl_radio.get(i).setChecked(false);
                cyl_text.get(i).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cyl_text.get(i).setTextIsSelectable(true);
            }
        }
    }


    private void radioIsChecked(View view) {
        if (view == rCapacity) {
            rDiameter.setChecked(false);
            rSpeed.setChecked(false);
            capacity.setInputType(InputType.TYPE_NULL);
            capacity.setTextIsSelectable(false);
            diameter.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            diameter.setTextIsSelectable(true);
            speed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            speed.setTextIsSelectable(true);
        } else if (view == rDiameter) {
            rCapacity.setChecked(false);
            rSpeed.setChecked(false);
            diameter.setInputType(InputType.TYPE_NULL);
            diameter.setTextIsSelectable(false);
            capacity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            capacity.setTextIsSelectable(true);
            speed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            speed.setTextIsSelectable(true);
        } else if (view == rSpeed) {
            rDiameter.setChecked(false);
            rCapacity.setChecked(false);
            speed.setInputType(InputType.TYPE_NULL);
            speed.setTextIsSelectable(false);
            diameter.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            diameter.setTextIsSelectable(true);
            capacity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            capacity.setTextIsSelectable(true);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_speed_capacity:
                try {
                    radioIsChecked(rCapacity);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.radio_speed_diametr:
                try {
                    radioIsChecked(rDiameter);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.radio_speed_Speed:
                try {
                    radioIsChecked(rSpeed);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.speed_diametr:
                diameter.setText("");
                break;
            case R.id.speed_capacity:
                capacity.setText("");
                break;
            case R.id.speed_Speed:
                speed.setText("");
                break;

            case R.id.radio_cyl_diameter:
                fillCylRadio(rad_cyl_diameter);
                break;
            case R.id.radio_cyl_height:
                fillCylRadio(rad_cyl_height);
                break;
            case R.id.radio_cyl_volume:
                fillCylRadio(rad_cyl_volume);
                break;

            case R.id.cyl_diameter:
                cyl_diameter.setText("");
                break;
            case R.id.cyl_height:
                cyl_height.setText("");
                break;
            case R.id.cyl_volume:
                cyl_volume.setText("");
                break;
        }
    }

    private void calculateCapacity() {
        if (rCapacity.isChecked() && diameter.getText().toString().length() > 0 && speed.getText().toString().length() > 0) {
            double diam = Double.parseDouble(diameter.getText().toString());
            double spd = Double.parseDouble(speed.getText().toString());
            capacity.setText(calculate.calcCapacity(diam, spd));
        }
    }

    private void calculateDiameter() {
        if (rDiameter.isChecked() && capacity.getText().toString().length() > 0 && speed.getText().toString().length() > 0) {
            double cpt = Double.parseDouble(capacity.getText().toString());
            double spd = Double.parseDouble(speed.getText().toString());
            diameter.setText(calculate.calcDiameter(cpt, spd));
        }
    }

    private void calculateSpeed() {
        if (rSpeed.isChecked() && diameter.getText().toString().length() > 0 && capacity.getText().toString().length() > 0) {
            double diam = Double.parseDouble(diameter.getText().toString());
            double cpt = Double.parseDouble(capacity.getText().toString());
            speed.setText(calculate.calcSpeed(cpt, diam));
        }
    }

    private void fillDoubleNumbers() {
        if (!cyl_diameter.getText().toString().equals("")) {
            cDiameter = Double.parseDouble(cyl_diameter.getText().toString());
        } else cDiameter = 0;
        if (!cyl_height.getText().toString().equals("")) {
            cHeight = Double.parseDouble(cyl_height.getText().toString());
        } else cHeight = 0;
        if (!cyl_volume.getText().toString().equals("")) {
            cVolume = Double.parseDouble(cyl_volume.getText().toString());
        } else cVolume = 0;
    }

    private void calcCylDiameter() {
        if (rad_cyl_diameter.isChecked()) {
            cyl_diameter.setText("");
        }
        if (rad_cyl_diameter.isChecked()
                && !cyl_height.getText().toString().equals("")
                && !cyl_volume.getText().toString().equals("")) {
            fillDoubleNumbers();
            cyl_diameter.setText(tube.calculateDiameter(cVolume, cHeight));
        }

    }

    private void calcCylHeight() {
        if (rad_cyl_height.isChecked()) {
            cyl_height.setText("");
        }
        if (rad_cyl_height.isChecked()
                && !cyl_diameter.getText().toString().equals("")
                && !cyl_volume.getText().toString().equals("")) {
            fillDoubleNumbers();
            cyl_height.setText(tube.calculateHeight(cVolume, cDiameter));
        }
    }

    private void calcCylVolume() {
        if (rad_cyl_volume.isChecked()
                && cyl_height.getText().toString().length() > 0
                && cyl_diameter.getText().toString().length() > 0) {
            fillDoubleNumbers();
            cyl_volume.setText(tube.calculateVolume(cDiameter, cHeight));
        }
    }



}