package com.example.helper.ui.holder;

public class CalculateHolder {

    public double calculateWidth (double capacity, double time, double diameter){
        double width = ((capacity/3600)*time)/(Math.PI *(Math.pow((diameter/2000),2)));
        return width;
    }

    public double calculateSpeed(double width, double time){
        double speed = width / time;
        return speed;
    }

    public double calculateVolume (double diameter, double width){
        double volume = Math.PI *(Math.pow((diameter/2000), 2)) * width;
        return volume;
    }

    public double calculateChangingTime (double width, double diameter, double chengingCapacity){
        double changingTime = (width * Math.PI * (Math.pow((diameter/2000), 2)))/(chengingCapacity/3600);
        return changingTime;
    }
}
