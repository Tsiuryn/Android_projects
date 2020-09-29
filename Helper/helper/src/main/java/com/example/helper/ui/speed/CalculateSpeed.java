package com.example.helper.ui.speed;

public class CalculateSpeed {

    protected String calcCapacity(double diameter, double speed){
        double capacity = (Math.PI * (Math.pow((diameter/1000),2))/4)*speed*3600;
        String cap = String.format("%.3f",capacity);
        return repl(cap);
    }

    protected String calcDiameter (double capacity, double speed){
        double diameter = Math.sqrt((4*capacity/3600)/(Math.PI*speed))*1000;
        String diam = String.format("%.3f", diameter);
        return repl(diam);
    }
    protected String calcSpeed (double capacity, double diameter){
        double speed = (4 * capacity)/ (Math.PI*Math.pow((diameter/1000),2)*3600);
        String sp = String.format("%.3f", speed);
        return repl(sp);
    }
    private String repl (String string){
        String repl = string.replaceAll(",", ".");
        return repl;
    }
}
