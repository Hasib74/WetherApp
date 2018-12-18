package com.example.monu.wetherapp.Hlper;

import android.util.Log;


import com.example.monu.wetherapp.Model.CurrentWeatherResponse;
import com.example.monu.wetherapp.Model.ForecastDTO;
import com.example.monu.wetherapp.Model.ForecastResponse;
import com.example.monu.wetherapp.Services.WeatherService;
import com.example.monu.wetherapp.UI.WeatherActivity;
import com.example.monu.wetherapp.fragments.CurrentWeatherFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Hard-won on 1/16/2018.
 */

public class WeatherServiceHelper {
    public static final String UNIT_METRIC = "metric";
    public static final String UNIT_IMPERIAL = "imperial";
    private static final String BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/";
    private static final String ENDPOINT_URL_CURRENT_WEATHER_DATA = "weather?";
    private static final String ENDPOINT_URL_FORECAST_WEATHER_DATA = "forecast?";
    private static final String API_KEY = "25eb9c3345b98e2cfd0c5f067277cd93";


    private WeatherService service;

    private WeatherResponseListener weatherResponseListener;

    private ForecastResponseListener forecastResponseListener;

    private Retrofit retrofit;

    public WeatherServiceHelper(WeatherResponseListener weatherResponseListener) {
        this.weatherResponseListener = weatherResponseListener;
    }

    public WeatherServiceHelper(ForecastResponseListener forecastResponseListener) {
        this.forecastResponseListener = forecastResponseListener;
    }

    public void getCurrentWeather(Double lat, Double lon) {
        String url, unitName = WeatherActivity.UNIT_SELECTED;
        // &units=metric
        url = ENDPOINT_URL_CURRENT_WEATHER_DATA
                + "lat="
                + WeatherActivity.latitude
                + "&lon="
                + WeatherActivity.longitude
                + "&units="
                + unitName
                + "&appid="
                + API_KEY;

        callAPIforCurrentWeather(url);
    }

    public void getCurrentWeather(String city) {
        String url, cityName, unitName = WeatherActivity.UNIT_SELECTED;
        cityName = city;
        url = ENDPOINT_URL_CURRENT_WEATHER_DATA
                + "q="
                + cityName
                + "&units="
                + unitName
                + "&appid="
                + API_KEY;

        callAPIforCurrentWeather(url);
    }

    private void callAPIforCurrentWeather(String url) {


        retrofit = new Retrofit.Builder()
                .baseUrl(WeatherServiceHelper.BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WeatherService.class);

        //Call<CurrentWeatherResponse> responseCall = service.getCurrentWeatherData(url);
        Call<CurrentWeatherResponse> responseCall = service.getCurrentWeatherData(url);

        responseCall.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call,
                                   Response<CurrentWeatherResponse> response) {
                if (response.code() == 200) {
                    CurrentWeatherResponse weatherData;
                    weatherData = response.body();
                    weatherResponseListener.onCurrentDataReceived(weatherData);
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callAPIforForecastWeather(String url) {

        retrofit = new Retrofit.Builder()
                .baseUrl(WeatherServiceHelper.BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WeatherService.class);

        Call<ForecastResponse> responseCall = service.getForecastWeatherData(url);

        responseCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse>
                    response) {
                Log.e("Error", "onResponse: called");
                if (response.code() == 200) {
                    ForecastResponse weatherData;
                    weatherData = response.body();

                    ArrayList<ForecastDTO> forecastList = new ArrayList<>();

                    for (int i = 0; i < weatherData.getCnt(); i++) {
                        ForecastResponse.List list = weatherData.getList().get(i);

                        String cityName, date, mainWeather, wind, icon, temperature, description,
                                max, min, pressure;
                        cityName = weatherData.getCity().getName();
                        date = list.getDtTxt();
                        mainWeather = list.getWeather().get(0).getMain();
                        wind = list.getWind().getSpeed().toString();
                        icon = list.getWeather().get(0).getIcon();
                        temperature = list.getMain().getTemp().toString();
                        description = list.getWeather().get(0).getDescription();
                        max = list.getMain().getTempMax().toString();
                        min = list.getMain().getTempMin().toString();
                        pressure = list.getMain().getPressure().toString();

                        ForecastDTO dto = new ForecastDTO(cityName,
                                date,
                                mainWeather,
                                wind,
                                icon,
                                temperature,
                                description,
                                max,
                                min,
                                pressure);

                        forecastList.add(dto);
                    }

                    forecastResponseListener.onForecastDataReceived(forecastList);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: called, " + t.getMessage());
            }
        });
    }

    public void getForecastWeather() {
        String url, cityName = CurrentWeatherFragment.cityName,
                unitName = WeatherActivity.UNIT_SELECTED;
        // &units=metric
        url = ENDPOINT_URL_FORECAST_WEATHER_DATA
                + "q="
                + cityName
                + "&units="
                + unitName
                + "&cnt="
                + WeatherActivity.FORECAST_LIMIT_SELECTED
                + "&appid="
                + API_KEY;

        callAPIforForecastWeather(url);
    }

    public void getForecastWeather(Double lat, Double lon) {
        String url, unitName = WeatherActivity.UNIT_SELECTED;
        // &units=metric
        url = ENDPOINT_URL_FORECAST_WEATHER_DATA
                + "lat="
                + WeatherActivity.latitude
                + "&lon="
                + WeatherActivity.longitude
                + "&units="
                + unitName
                + "&cnt="
                + WeatherActivity.FORECAST_LIMIT_SELECTED
                + "&appid="
                + API_KEY;

        callAPIforForecastWeather(url);
    }

    public interface WeatherResponseListener {
        void onCurrentDataReceived(CurrentWeatherResponse data);
    }

    public interface ForecastResponseListener {
        void onForecastDataReceived(ArrayList<ForecastDTO> forecastList);
    }
}
