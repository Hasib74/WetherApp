package com.example.monu.wetherapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.monu.wetherapp.Adepter.ForecastAdapter;
import com.example.monu.wetherapp.Hlper.WeatherServiceHelper;
import com.example.monu.wetherapp.Model.ForecastDTO;
import com.example.monu.wetherapp.R;
import com.example.monu.wetherapp.UI.WeatherActivity;

import java.util.ArrayList;

public class ForecastWeatherFragment extends Fragment implements WeatherServiceHelper
        .ForecastResponseListener {

    private ArrayList<ForecastDTO> forecastList;

    private RecyclerView rvForecastWeather;

    private WeatherServiceHelper serviceHelper;

    private ForecastAdapter adapter;

    public ForecastWeatherFragment() {
        // Required empty public constructor
    }

    public static ForecastWeatherFragment getInstance() {
        Bundle bundle = new Bundle();
        ForecastWeatherFragment fragment = new ForecastWeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);

        // Initiating View
        rvForecastWeather = view.findViewById(R.id.rvForecastWeather);

        serviceHelper = new WeatherServiceHelper(this);

        if (WeatherActivity.getWeatherUsingLatLon)
            getForecastWeather(WeatherActivity.latitude, WeatherActivity.longitude);
        else
            getForecastWeather();

        // Inflate the layout for this fragment
        return view;


    }

    private void getForecastWeather() {
        serviceHelper.getForecastWeather();
    }

    private void getForecastWeather(Double lat, Double lon) {
        serviceHelper.getForecastWeather(lat, lon);
    }

    @Override
    public void onForecastDataReceived(ArrayList<ForecastDTO> forecastList) {
        this.forecastList = forecastList;

        adapter = new ForecastAdapter(getContext(), forecastList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        rvForecastWeather.setLayoutManager(manager);
        rvForecastWeather.setAdapter(adapter);
    }
}
