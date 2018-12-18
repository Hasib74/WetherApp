package com.example.monu.wetherapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monu.wetherapp.Hlper.WeatherServiceHelper;
import com.example.monu.wetherapp.Model.CurrentWeatherResponse;

import com.example.monu.wetherapp.R;
import com.example.monu.wetherapp.Tools;
import com.example.monu.wetherapp.UI.WeatherActivity;
import com.squareup.picasso.Picasso;


public class CurrentWeatherFragment extends Fragment implements WeatherServiceHelper.WeatherResponseListener {

    /*private static final String KEY_TEMPERATURE = "temp";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_CITYF = "city";
    private static final String KEY_WEATHER_MAIN = "main";
    private static final String KEY_WIND = "wind";
    private static final String KEY_ICON = "icon";
    private static final String KEY_MAX = "max";
    private static final String KEY_MIN = "min";
    private static final String KEY_PRESSURE = "pressure";*/

    public static final String KEY_CITYF = "city";
    public static String cityName;
    // Views
    private TextView tvTemperature;
    private TextView tvDate;
    private TextView tvHumidity;
    private TextView tvCityF;
    private TextView tvWeatherMain;
    private TextView tvWind;
    private ImageView ivIcon;
    private TextView tvTime;
    private TextView tvMaxMin;
    private TextView tvPressure;
    private WeatherServiceHelper serviceHelper;

    private CurrentWeatherResponse currentWeatherResponse;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment getInstance() {
        Bundle bundle = new Bundle();

        /*bundle.putString(KEY_TEMPERATURE, temperature);
        String date = Tools.convertDate(dateTimestamp);
        String time = Tools.convertTime(dateTimestamp);
        bundle.putString(KEY_DATE, date);
        bundle.putString(KEY_TIME, time);
        bundle.putString(KEY_HUMIDITY, String.valueOf(humidity));
        bundle.putString(KEY_CITYF, city);
        bundle.putString(KEY_WEATHER_MAIN, mainWeather);
        bundle.putString(KEY_WIND, String.valueOf(wind));
        bundle.putString(KEY_ICON, icon);
        bundle.putString(KEY_MAX, String.valueOf(max));
        bundle.putString(KEY_MIN, String.valueOf(min));
        bundle.putString(KEY_PRESSURE, String.valueOf(pressure));*/

        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CurrentWeatherFragment getInstance(String city) {
        Bundle bundle = new Bundle();

        bundle.putString(KEY_CITYF, city);

        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        // setting the city name
        try {
            cityName = getArguments().getString(KEY_CITYF);
        } catch (Exception e) {
            Log.e("tut", "onCreateView: getArguments - " + e.getMessage());
        }

        if (cityName == null) {
            cityName = "Dhaka,BD";
        }

        initViews(view);

        serviceHelper = new WeatherServiceHelper(this);

        if (WeatherActivity.getWeatherUsingLatLon)
            getWeatherData(WeatherActivity.latitude, WeatherActivity.longitude);
        else
            getWeatherData(cityName);


        // Inflate the layout for this fragment
        return view;
    }

    private void getWeatherData(String cityName) {
        if (isNetworkAvailable()) {
            serviceHelper.getCurrentWeather(cityName);
        } else {
            Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void getWeatherData(Double lat, Double lon) {
        if (isNetworkAvailable()) {
            serviceHelper.getCurrentWeather(lat, lon);
        } else {
            Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initViews(View view) {
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvDate = view.findViewById(R.id.tvCurrentDate);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvCityF = view.findViewById(R.id.tvCityF);
        tvWeatherMain = view.findViewById(R.id.tvWeatherMain);
        tvWind = view.findViewById(R.id.tvWind);
        ivIcon = view.findViewById(R.id.ivIcon);
        tvTime = view.findViewById(R.id.tvTime);
        tvMaxMin = view.findViewById(R.id.tvMaxMin);
        tvPressure = view.findViewById(R.id.tvPressure);
    }

    @Override
    public void onCurrentDataReceived(CurrentWeatherResponse data) {
        this.currentWeatherResponse = data;

        setValuesToViews();
    }

    private void setValuesToViews() {

        String temperature = currentWeatherResponse.getMain().getTemp().toString();
        String date = Tools.convertDate(currentWeatherResponse.getDt());
        String time = Tools.convertTime(currentWeatherResponse.getDt());
        String humidity = String.valueOf(currentWeatherResponse.getMain().getHumidity());
        String city = currentWeatherResponse.getName();
        String mainWeather = currentWeatherResponse.getWeather().get(0).getMain();
        String wind = String.valueOf(currentWeatherResponse.getWind().getSpeed());
        String icon = currentWeatherResponse.getWeather().get(0).getIcon();
        String max = String.valueOf(currentWeatherResponse.getMain().getTempMax());
        String min = String.valueOf(currentWeatherResponse.getMain().getTempMin());
        String pressure = String.valueOf(currentWeatherResponse.getMain().getPressure());


        // set the values

        if (WeatherActivity.UNIT_SELECTED.equals(WeatherServiceHelper.UNIT_METRIC)) {
            tvTemperature.setText(temperature + "°C");
            tvDate.setText(date);
            tvHumidity.setText("Humidity: " + humidity + "%");
            tvCityF.setText(city);
            tvWeatherMain.setText(mainWeather);
            tvWind.setText("Wind: " + wind + "meter/sec");
            tvTime.setText(time);
            tvMaxMin.setText("Max/Min: " + max + "°C/" + min + "°C");
            tvPressure.setText(pressure + " hPa");
        } else {
            tvTemperature.setText(temperature + "°F");
            tvDate.setText(date);
            tvHumidity.setText("Humidity: " + humidity + "%");
            tvCityF.setText(city);
            tvWeatherMain.setText(mainWeather);
            tvWind.setText("Wind: " + wind + "miles/hour");
            tvTime.setText(time);
            tvMaxMin.setText("Max/Min: " + max + "°F/" + min + "°F");
            tvPressure.setText(pressure + " hPa");
        }


        // set the icon
        try {
            Uri uri = Uri.parse("http://api.openweathermap.org/img/w/" + icon);
            Picasso.with(getContext()).load(uri).into(ivIcon);
        } catch (Exception e) {
            Log.e("abc", "onCreateView: Image Load failed! " + e.getMessage());
        }
    }
}
