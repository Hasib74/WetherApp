package com.example.monu.wetherapp.Hlper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static com.example.monu.wetherapp.UI.WeatherActivity.latitude;
import static com.example.monu.wetherapp.UI.WeatherActivity.longitude;


/**
 * Created by PrinceOfNightmareH on 12-Feb-18.
 */

public class LocationServiceHelper {

    private LocationResponseListener mListener;
    private Context mContext;

    private FusedLocationProviderClient mClient;
    private LocationCallback mCallback;

    public LocationServiceHelper(Context context, LocationResponseListener listener) {
        mContext = context;
        mListener = listener;
    }

    @SuppressLint("MissingPermission")
    public void getLatLon() {

        mClient = LocationServices.getFusedLocationProviderClient(mContext);


        LocationRequest mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(1000);

        mCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        if (latitude == 0 && longitude == 0 && location.getLatitude() != 0 &&
                                location.getLongitude() != 0) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            mListener.onLatLonReceived(latitude, longitude);


                            Toast.makeText(mContext, "Location Found", Toast
                                    .LENGTH_SHORT).show();
                            Log.d("location", "onLocationResult: " + latitude + " " + longitude);
//                            stopLocationUpdates();
                        }

                        else {
                            mListener.onLatLonReceived(latitude, longitude);
                        }

                        Log.d("location", "onLocationResult: " + latitude + " " + longitude);
                        // return;
                    } else {
                        Toast.makeText(mContext, "Please Enable GPS", Toast
                                .LENGTH_SHORT).show();
                        Log.d("location", "onLocationResult: null");
                    }
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission
                .ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mClient.requestLocationUpdates(mLocationRequest, mCallback, null);
        Log.d("location", "getLocation: " + latitude + " " + longitude);
    }


    public void stopLocationUpdates() {
        if (mClient != null && mCallback != null) {
            mClient.removeLocationUpdates(mCallback);
        }
    }


    public interface LocationResponseListener {
        void onLatLonReceived(double lat, double lon);
    }
}
