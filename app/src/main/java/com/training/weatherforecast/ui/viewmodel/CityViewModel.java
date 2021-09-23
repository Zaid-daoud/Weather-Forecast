package com.training.weatherforecast.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.training.weatherforecast.data.repo.CityRepository;
import com.training.weatherforecast.model.CityModel;

import java.util.List;

public class CityViewModel extends AndroidViewModel {
    private CityRepository repository;
    private LiveData<List<CityModel>> allCities;

    public CityViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
    }

    public void insert(CityModel cityModel){
        repository.insert(cityModel);
    }
    public void delete(CityModel cityModel){
        repository.delete(cityModel);
    }
    public void deleteAllCities(){
        repository.deleteAllCities();
    }
    public LiveData<List<CityModel>> getAllCities(){
        return allCities;
    }
    public CityRepository getRepository(){
        return repository;
    }
}
