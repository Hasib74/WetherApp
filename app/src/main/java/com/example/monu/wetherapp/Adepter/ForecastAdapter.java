package com.example.monu.wetherapp.Adepter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monu.wetherapp.Hlper.WeatherServiceHelper;
import com.example.monu.wetherapp.Model.ForecastDTO;
import com.example.monu.wetherapp.R;
import com.example.monu.wetherapp.UI.WeatherActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hard-won on 1/22/2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private Context context;
    private ArrayList<ForecastDTO> list;

    public ForecastAdapter(Context context, ArrayList<ForecastDTO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_row_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        ForecastDTO dto = list.get(position);

        String icon = dto.getIcon();

        Uri uri = Uri.parse("http://api.openweathermap.org/img/w/" + icon);
        Picasso.with(context).load(uri).into(holder.ivIcon);

        if (WeatherActivity.UNIT_SELECTED.equals(WeatherServiceHelper.UNIT_METRIC)) {
            holder.tvCityF.setText(dto.getCityName());
            holder.tvDate.setText(dto.getDate());
            holder.tvWeatherMain.setText(dto.getMainWeather());
            holder.tvWind.setText("Wind : " + dto.getWind() + "m/s");
            holder.tvTemperature.setText(dto.getTemperature() + "°C");
            holder.tvWeatherDescription.setText(dto.getDescription());
            holder.tvMaxMin.setText(dto.getMax() + "°C/" + dto.getMin() + "°C");
            holder.tvPressure.setText("Pressure : " + dto.getPressure() + "hPa");
        } else {
            holder.tvCityF.setText(dto.getCityName());
            holder.tvDate.setText(dto.getDate());
            holder.tvWeatherMain.setText(dto.getMainWeather());
            holder.tvWind.setText("Wind : " + dto.getWind() + "meter/sec");
            holder.tvTemperature.setText(dto.getTemperature() + "°F");
            holder.tvWeatherDescription.setText(dto.getDescription());
            holder.tvMaxMin.setText(dto.getMax() + "°F/" + dto.getMin() + "°F");
            holder.tvPressure.setText("Pressure : " + dto.getPressure() + "hPa");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView tvCityF;
        TextView tvDate;
        TextView tvWeatherMain;
        TextView tvWind;
        ImageView ivIcon;
        TextView tvTemperature;
        TextView tvWeatherDescription;
        TextView tvMaxMin;
        TextView tvPressure;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            tvCityF = itemView.findViewById(R.id.tvCityF);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWeatherMain = itemView.findViewById(R.id.tvWeatherMain);
            tvWind = itemView.findViewById(R.id.tvWind);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvWeatherDescription = itemView.findViewById(R.id.tvWeatherDescription);
            tvMaxMin = itemView.findViewById(R.id.tvMaxMin);
            tvPressure = itemView.findViewById(R.id.tvPressure);
        }
    }
}
