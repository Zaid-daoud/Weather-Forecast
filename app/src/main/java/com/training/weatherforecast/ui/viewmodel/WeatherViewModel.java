package com.training.weatherforecast.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.training.weatherforecast.data.repo.CityRepository;

public class WeatherViewModel extends AndroidViewModel {
    private CityRepository repository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
    }
    public CityRepository getRepository(){
        return repository;
    }
}
