package com.example.helper.ui.speed;

public class CalculateTube {
    protected String calculateVolume (double diameter, double height){
        double volume = Math.PI * (Math.pow(diameter/1000, 2)/4) * height * 1000;
        String sVolume = String.format("%.4f", volume);
        return repl(sVolume);
    }

    protected String calculateHeight (double volume, double diameter){
        double height = (4 * volume/1000)/ (Math.PI * (Math.pow(diameter/1000, 2)));
        String sHeight = String.format("%.3f", height);
        return repl(sHeight);
    }


    protected String calculateDiameter (double volume, double height){
        double diameter = Math.sqrt((4*volume/1000)/(Math.PI * height)) * 1000;
        String sDiameter = String.format("%.3f", diameter);
        return repl(sDiameter);
    }

    private String repl (String string){
        String repl = string.replaceAll(",", ".");
        return repl;
    }
}
