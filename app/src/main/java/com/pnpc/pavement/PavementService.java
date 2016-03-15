package com.pnpc.pavement;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by jjshin on 2/21/16.
 */
public class PavementService extends Service implements com.google.android.gms.location.LocationListener, SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    SensorManager sensorManager;
    LocationManager locManager;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    ArrayList<Float> xArray = new ArrayList<>();
    ArrayList<Float> yArray = new ArrayList<>();
    ArrayList<Float> zArray = new ArrayList<>();
    Double startLat;
    Double endLat;
    Double startLng;
    Double endLng;
    final static int RIDE_ID = 179;
    Retrofit retrofit;
    ReadingService readingService;
    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, 500000);

        locationRequest = createLocationRequest();
        Log.i("network", "" + isOnline(this));
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
            Log.i("GoogleAPIClient", "" + googleApiClient);
        }

        readingService = ServiceGenerator.createService(ReadingService.class, "", "");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        stopLocationUpdates();
    }


    public void setupLocationRequest() {
        Log.i("GPS Permission", "" + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        Log.i("GPS Permission result", "" + PackageManager.PERMISSION_GRANTED);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        return;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i("Sensor Test", "SensorUpdate: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
            xArray.add(event.values[0]);
            yArray.add(event.values[1]);
            zArray.add(event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("GPS on location changed", "Latitude: " + location.getLatitude());
        Log.i("GPS on location changed", "Longitude: = " + location.getLongitude());
        if(startLat == null || startLng == null){
            startLat = location.getLatitude();
            startLng = location.getLongitude();
            return;
        }

        endLat = location.getLatitude();
        endLng = location.getLongitude();

        JSONObject reading = makeReadingJson(startLat, startLng, endLat, endLng, xArray.toString(), yArray.toString(), zArray.toString());
        Log.i("reading", "" + reading.toString());
//        Call<Reading> call = readingService.createReading(startLat, startLng, endLat, endLng, xArray.toString(), yArray.toString(), zArray.toString(), RIDE_ID);
        Call<Reading> call = readingService.createReading(reading);
        call.enqueue(new Callback<Reading>() {

                         @Override
                         public void onResponse(Call<Reading> call, retrofit2.Response<Reading> response) {
                             Log.i("Retrofit onResponse", "successful" + response.body() + ";" + response.errorBody());
                         }

                         @Override
                         public void onFailure(Call<Reading> call, Throwable t) {
                             Log.i("Retrofit onFailure", "Well, that didn't work");
                         }
                     });
        clearArrays();

        startLat = endLat;
        startLng = endLng;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("GPS googleclient", "GoogleClientApi connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GPS googleclient", "GoogleClientApi connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GPS googleclient", "GoogleClientApi connection failed");

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i("GPS startupdates", "Somehow the permissions are missing");
            return;
        }
        Log.i("GPS startUpdates", "updates request about to be started");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(900);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected void clearArrays(){
        xArray.clear();
        yArray.clear();
        zArray.clear();
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }


    public JSONObject makeReadingJson(double startLat, double startLng, double endLat, double endLng, String xArray, String yArray, String zArray){
        JSONObject readingJson = new JSONObject();

        try {
                readingJson.put("acceleration_x", xArray);
                readingJson.put("acceleration_y", yArray);
                readingJson.put("acceleration_z", zArray);
                readingJson.put("start_lon", startLng);
                readingJson.put("start_lat", startLat);
                readingJson.put("end_lat", endLat);
                readingJson.put("end_lat", endLng);
                readingJson.put("ride_id", RIDE_ID);
            } catch (JSONException e) {
                  // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.i("Json test", readingJson.toString());

            return readingJson;
        }
}
