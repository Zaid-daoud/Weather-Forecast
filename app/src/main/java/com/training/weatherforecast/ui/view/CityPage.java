package com.training.weatherforecast.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.training.weatherforecast.R;
import com.training.weatherforecast.data.api.WeatherInterface;
import com.training.weatherforecast.databinding.ActivityCityPageBinding;
import com.training.weatherforecast.ui.viewmodel.WeatherViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CityPage extends AppCompatActivity {

    private ActivityCityPageBinding binding;
    private WeatherViewModel weatherViewModel;
    private static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_page);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_city_page);
        String cityName = getIntent().getStringExtra("cityName");
        URL = "http://api.weatherapi.com/v1/forecast.json?key=ac79065e5402474ca05163838212109&q="+ cityName +"&days=6&aqi=no&alerts=no";
        binding.toolBar.setTitle(cityName);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        showWeatherInfo(URL);

        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showWeatherInfo(URL);
                binding.refresh.setRefreshing(false);
            }
        });
    }

    private void showWeatherInfo(String URL) {
        weatherViewModel.getRepository().sendApiRequest(URL, new WeatherInterface() {
            @Override
            public void setJsonDataResponse(JSONObject response) {
                try {
                    int current = response.getJSONObject("current").getInt("is_day");
                    if(current == 1){
                        binding.background.setBackgroundResource(R.drawable.day_background);
                    }else {
                        binding.background.setBackgroundResource(R.drawable.night_background);
                    }
                    showCurrent(response.getJSONObject("location"),response.getJSONObject("current"));
                    showForecasts(response.getJSONObject("forecast"));
                } catch (JSONException e) {
                    Toast.makeText(CityPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void setVolleyError(VolleyError error) {
                Toast.makeText(CityPage.this,"Something went wrong .. please check the name of the city",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCurrent(JSONObject location, JSONObject current) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(location.getString("localtime"));
            String date  = new SimpleDateFormat("yyyy-MM-dd").format(d);
            String time  = new SimpleDateFormat("HH:mm").format(d);
            binding.current.currentDate.setText(date);
            binding.current.currentTime.setText(time);
            binding.current.currentTemp.setText(current.getString("temp_c") +" °C");
            binding.current.currentFeels.setText(current.getString("feelslike_c") +" °C");
            binding.current.currentWindSpeed.setText(current.getString("wind_kph") +" kph");
            binding.current.currentCondition.setText(current.getJSONObject("condition").getString("text"));
            Picasso.get().load("https://"+current.getJSONObject("condition").getString("icon")).into(binding.current.conditionImg);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void showForecasts(JSONObject forecast) {
        try {
            JSONArray jsonArray = forecast.getJSONArray("forecastday");
            setDayInfo(jsonArray , 1);
            setDayInfo(jsonArray , 2);
            setDayInfo(jsonArray , 3);
            setDayInfo(jsonArray , 4);
            setDayInfo(jsonArray , 5);
        }catch (JSONException e) {
            Toast.makeText(CityPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void setDayInfo(JSONArray jsonArray ,int day) {
         try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(jsonArray.getJSONObject(day).getString("date"));
            String date = new SimpleDateFormat("yyyy-MM-dd").format(d);
            switch (day){
                case 1:
                    binding.forecasts.firstDayDate.setText(date);
                    binding.forecasts.firstDayTemp.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c") + " °C");
                    Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(binding.forecasts.firstConditionImage);
                    binding.forecasts.firstDayDetailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonArray,day);
                        }
                    });
                    break;
                case 2:
                    binding.forecasts.secondDayDate.setText(date);
                    binding.forecasts.secondDayTemp.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c") + " °C");
                    Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(binding.forecasts.secondConditionImage);
                    binding.forecasts.secondDayDetailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonArray,day);
                        }
                    });
                    break;
                case 3:
                    binding.forecasts.thirdDayDate.setText(date);
                    binding.forecasts.thirdDayDate.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c") + " °C");
                    Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(binding.forecasts.thirdConditionImage);
                    binding.forecasts.thirdDayDetailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonArray,day);
                        }
                    });
                    break;
                case 4:
                    binding.forecasts.fourthDayDate.setText(date);
                    binding.forecasts.fourthDayTemp.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c") + " °C");
                    Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(binding.forecasts.fourthConditionImage);
                    binding.forecasts.fourthDayDetailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonArray,day);
                        }
                    });
                    break;
                case 5:
                    binding.forecasts.fifthDayDate.setText(date);
                    binding.forecasts.fifthDayTemp.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c") + " °C");
                    Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(binding.forecasts.fifthConditionImage);
                    binding.forecasts.fifthDayDetailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog(jsonArray,day);
                        }
                    });
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openDialog(JSONArray jsonArray, int day) {
        Dialog dialog = new Dialog(CityPage.this);
        dialog.setContentView(R.layout.more_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView dateTxT = dialog.findViewById(R.id.date);
        TextView sunriseTxT = dialog.findViewById(R.id.sunrise);
        TextView sunsetTxT = dialog.findViewById(R.id.sunset);
        TextView maxTempTxT = dialog.findViewById(R.id.max_temp);
        TextView avgTempTxT = dialog.findViewById(R.id.avg_temp);
        TextView minTempTxT = dialog.findViewById(R.id.min_temp);
        TextView maxWindSpeedTxT = dialog.findViewById(R.id.max_wind);
        ImageView condition_image = dialog.findViewById(R.id.condition_image);
        ImageView close = dialog.findViewById(R.id.closeDialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(jsonArray.getJSONObject(day).getString("date"));
            String date = new SimpleDateFormat("yyyy-MM-dd").format(d);
            dateTxT.setText(date);
            Picasso.get().load("https://"+jsonArray.getJSONObject(day).getJSONObject("day").getJSONObject("condition").getString("icon")).into(condition_image);
            sunriseTxT.setText(jsonArray.getJSONObject(day).getJSONObject("astro").getString("sunrise"));
            sunsetTxT.setText(jsonArray.getJSONObject(day).getJSONObject("astro").getString("sunset"));
            maxTempTxT.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("maxtemp_c")+" °C");
            avgTempTxT.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("avgtemp_c")+" °C");
            minTempTxT.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("mintemp_c")+" °C");
            maxWindSpeedTxT.setText(jsonArray.getJSONObject(day).getJSONObject("day").getString("maxwind_kph")+" kph");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.show();
    }
}