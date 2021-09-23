package com.training.weatherforecast.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "city_table")
public class CityModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public CityModel(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
