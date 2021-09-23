package com.training.weatherforecast.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.training.weatherforecast.model.CityModel;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insert(CityModel cityModel);

    @Delete
    void delete(CityModel cityModel);

    @Query("DELETE FROM city_table")
    void deleteAllCities();

    @Query("SELECT * FROM city_table")
    LiveData<List<CityModel>> getAllCities();
}
