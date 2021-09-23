package com.training.weatherforecast.data.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.training.weatherforecast.data.api.WeatherClient;
import com.training.weatherforecast.data.api.WeatherInterface;
import com.training.weatherforecast.data.database.CityDao;
import com.training.weatherforecast.data.database.CityDatabase;
import com.training.weatherforecast.model.CityModel;

import org.json.JSONObject;

import java.util.List;

public class CityRepository {
    private CityDao cityDao;
    private LiveData<List<CityModel>> allCities;
    private Application application;

    public CityRepository(Application application){
        this.application = application;
        CityDatabase cityDatabase = CityDatabase.getInstance(application);
        cityDao = cityDatabase.cityDao();
        allCities = cityDao.getAllCities();
    }

    public void sendApiRequest(String URL,WeatherInterface weatherInterface){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherInterface.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherInterface.setVolleyError(error);
            }
        });
        WeatherClient.getInstance(application).addToRequest(jsonObjectRequest);
    }

    public void insert(CityModel cityModel){
        new InsertCityAsyncTask(cityDao).execute(cityModel);
    }
    public void delete(CityModel cityModel){
        new DeleteCityAsyncTask(cityDao).execute(cityModel);
    }
    public void deleteAllCities(){
        new DeleteAllCitiesAsyncTask(cityDao).execute();
    }

    public LiveData<List<CityModel>> getAllCities(){
        return allCities;
    }

    private static class InsertCityAsyncTask extends AsyncTask<CityModel,Void,Void>{
        private CityDao cityDao;
        public InsertCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(CityModel... cities) {
            cityDao.insert(cities[0]);
            return null;
        }
    }

    private static class DeleteCityAsyncTask extends AsyncTask<CityModel,Void,Void>{
        private CityDao cityDao;
        public DeleteCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(CityModel... cities) {
            cityDao.delete(cities[0]);
            return null;
        }
    }

    private static class DeleteAllCitiesAsyncTask extends AsyncTask<Void,Void,Void>{
        private CityDao cityDao;
        public DeleteAllCitiesAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cityDao.deleteAllCities();
            return null;
        }
    }

}
