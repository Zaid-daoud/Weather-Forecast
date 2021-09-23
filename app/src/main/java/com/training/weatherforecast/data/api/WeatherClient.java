package com.training.weatherforecast.data.api;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class WeatherClient {

    private static WeatherClient INSTANCE;
    private RequestQueue requestQueue;
    private Application application;

    public WeatherClient(Application application) {
        this.application = application;
    }

    public static synchronized WeatherClient getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new WeatherClient(application);
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(application);
        }
        return requestQueue;
    }

    public void addToRequest(JsonObjectRequest jsonObjectRequest){
        getRequestQueue().add(jsonObjectRequest);
    }
}
