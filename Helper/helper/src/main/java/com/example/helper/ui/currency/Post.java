package com.example.helper.ui.currency;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("Cur_ID")
    private int cure_Id;

    @SerializedName("Date")
    private String date;

    @SerializedName("Cur_Abbreviation")
    private String cur_abbreviation;

    @SerializedName("Cur_Scale")
    private int cur_scale;

    @SerializedName("Cur_OfficialRate")
    private double cur_officialRate;

    public Post(int cure_Id, String date, String cur_abbreviation, int cur_scale, double cur_officialRate) {
        this.cure_Id = cure_Id;
        this.date = date;
        this.cur_abbreviation = cur_abbreviation;
        this.cur_scale = cur_scale;
        this.cur_officialRate = cur_officialRate;
    }

    public int getCure_Id() {
        return cure_Id;
    }

    public String getDate() {
        return date;
    }

    public String getCur_abbreviation() {
        return cur_abbreviation;
    }

    public int getCur_scale() {
        return cur_scale;
    }

    public double getCur_officialRate() {
        return cur_officialRate;
    }
}
