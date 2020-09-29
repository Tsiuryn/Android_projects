package com.example.helper.ui.normalization;

public class CalculateNormalization {

    protected String quantMilk (double quantCream, double quantSkim, double fCream, double fMilk, double fSkim){
        if (quantSkim <=  0){
            double quantMilk = quantCream * (fCream - fSkim) / (fMilk - fSkim);
            String milk = String.format("%.3f", quantMilk);
            return repl(milk);
        }
        else if (quantCream <= 0){
            double quantMilk = quantSkim * (fCream - fSkim) / (fCream - fMilk);
            String milk = String.format("%.3f", quantMilk);
            return repl(milk);
        }
        return null;
    }

    protected String quantCream (double quantMilk, double quantSkim, double fCream, double fMilk, double fSkim){
        if (quantSkim <=  0){
            double quantCream = quantMilk * (fMilk - fSkim) / (fCream - fSkim);
            String cream = String.format("%.3f", quantCream);
            return repl(cream);
        }
        else if (quantMilk <= 0){
            double quantCream = quantSkim * (fMilk - fSkim) / (fCream - fMilk);
            String cream = String.format("%.3f", quantCream);
            return repl(cream);
        }
        return null;
    }


    protected String quantSkim (double quantCream, double quantMilk, double fCream, double fMilk, double fSkim){
        if (quantMilk <=  0){
            double quantSkim = quantCream * (fCream - fMilk) / (fMilk - fSkim);
            String skim = String.format("%.3f", quantSkim);
            return repl(skim);
        }
        else if (quantCream <= 0){
            double quantSkim = quantMilk * (fCream - fMilk) / (fCream - fSkim);
            String skim = String.format("%.3f", quantSkim);
            return repl(skim);
        }
        return null;
    }

    protected String fatCream (double quantSkim, double quantMilk, double quantCream, double fMilk, double fSkim){

        if (quantSkim <= 0){
            double fCream = quantMilk * (fMilk - fSkim) / quantCream + fSkim;
            String fatCream = String.format("%.3f", fCream);
            return repl(fatCream);
        }
        else if (quantMilk <= 0){
            double fCream = quantSkim * (fMilk - fSkim) / quantCream + fMilk;
            String fatCream = String.format("%.3f", fCream);
            return repl(fatCream);
        }

        return null;
    }

    protected String fatMilk (double quantSkim, double quantMilk, double quantCream, double fCream, double fSkim){
        if (quantSkim <= 0){
            double fMilk = quantCream * (fCream - fSkim) / quantMilk + fSkim;
            String fatCream = String.format("%.3f", fMilk);
            return repl(fatCream);
        }
        else if (quantCream <= 0){
            double fMilk = fCream -  quantSkim * (fCream - fSkim) / quantMilk;
            String fatCream = String.format("%.3f", fMilk);
            return repl(fatCream);
        }

        return null;
    }

    protected String fatSkim (double quantMilk, double quantCream, double quantSkim, double fCream, double fMilk){
        if (quantMilk <= 0){
            double fSkim = fMilk - quantCream * (fCream - fMilk) / quantSkim ;
            String fatCream = String.format("%.3f", fSkim);
            return repl(fatCream);
        }
        else if (quantCream <= 0){
            double fSkim = fCream - quantMilk * (fCream - fMilk) / quantSkim ;
            String fatCream = String.format("%.3f", fSkim);
            return repl(fatCream);
        }

        return null;
    }

    private String repl (String string){
        String repl = string.replaceAll(",", ".");
        return repl;
    }
}
