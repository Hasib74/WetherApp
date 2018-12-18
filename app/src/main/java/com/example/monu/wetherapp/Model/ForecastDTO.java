package com.example.monu.wetherapp.Model;

/**
 * Created by Hard-won on 1/22/2018.
 */

public class ForecastDTO {

    private String cityName, date, mainWeather, wind, icon, temperature, description, max, min,
            pressure;

    public ForecastDTO(String cityName, String date, String mainWeather, String wind, String
            icon, String temperature, String description, String max, String min, String
            pressure) {
        this.cityName = cityName;
        this.date = date;
        this.mainWeather = mainWeather;
        this.wind = wind;
        this.icon = icon;
        this.temperature = temperature;
        this.description = description;
        this.max = max;
        this.min = min;
        this.pressure = pressure;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDate() {
        return date;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public String getWind() {
        return wind;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    public String getPressure() {
        return pressure;
    }
}
