package com.example.helper.ui.calculator.my_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "calc")
public class Calculations {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "edit")
    private String edit;

    @ColumnInfo(name = "result")
    private String result;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "description")
    private String description;

    public Calculations(){}

    @Ignore
    public Calculations(String edit, String result, String date, String description) {
        this.edit = edit;
        this.result = result;
        this.date = date;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEdit() {
        return edit;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
