package com.training.weatherforecast.data.api;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface WeatherInterface {
    void setJsonDataResponse(JSONObject response);
    void setVolleyError(VolleyError error);
}
