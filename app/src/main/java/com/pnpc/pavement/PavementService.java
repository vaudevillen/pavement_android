package com.pnpc.pavement;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jjshin on 2/21/16.
 */
public class PavementService extends Service implements com.google.android.gms.location.LocationListener, SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences sharedPreferences;
    SensorManager sensorManager;
    LocationManager locManager;
    GoogleApiClient googleApiClient;
    PavementAPIService pavementAPIService;
    LocationRequest locationRequest;
    int calibrationId;
    int scoreboardId;
    ArrayList<Float> xArray = new ArrayList<>();
    ArrayList<Float> yArray = new ArrayList<>();
    ArrayList<Float> zArray = new ArrayList<>();
    Double startLat;
    double endLat;
    Double startLng;
    double endLng;
    float angleX;
    float angleY;
    float angleZ;
    double startTime;
    double endTime;
    int rideId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Pavement service starting", Toast.LENGTH_SHORT).show();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, accelerometerSensor, 500000);
        sensorManager.registerListener(this, gyroscopeSensor, 500000);

        locationRequest = createLocationRequest();
        Log.i("network", "" + isOnline(this));
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        pavementAPIService = PavementAPIServiceGenerator.createService(PavementAPIService.class, "peemster", "halsadick");
        final Ride ride = new Ride();
        ride.setStartTime(System.currentTimeMillis() / 1000);
        Call<Ride> createRideCall = pavementAPIService.createRide(ride);
        Log.i("Ride", "id" + ride.getId() + ", calibration" + ride.getCalibrationId() + ", endTime " + ride.getEndTime() + ", scoreboard" + ride.getScoreboardId() + ", startTime" + ride.getStartTime());
        createRideCall.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                Log.i("createRide onResponse", "Ride: onSuccess: " + response.body() + "; onError: " + response.errorBody());
                Ride savedRide = response.body();
                rideId = savedRide.getId();
                updateIds();
                ride.setCalibrationId(calibrationId);
                ride.setScoreboardId(scoreboardId);
                putRideRequest(rideId, ride);
//                googleApiClient connect called here to make sure rideId isn't null
                googleApiClient.connect();
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                Log.i("createRide onFailure", "Create ride failed");
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        stopLocationUpdates();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i("Sensor Test", "SensorUpdate: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
            xArray.add(event.values[0]);
            yArray.add(event.values[1]);
            zArray.add(event.values[2]);
        }
        else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            angleX = event.values[0];
            angleY = event.values[1];
            angleZ = event.values[2];
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
            startTime = getCurrentTime();
            return;
        }

        endLat = location.getLatitude();
        endLng = location.getLongitude();

        endTime = getCurrentTime();

        xArray = trimArray(xArray);
        yArray = trimArray(yArray);
        zArray = trimArray(zArray);

        Reading reading = new Reading();
        reading.setRideId(rideId);
        reading.setAccelerations(xArray, yArray, zArray);
        reading.setEndLat(endLat);
        reading.setEndLon(endLng);
        reading.setStartLat(startLat);
        reading.setStartLon(startLng);
        reading.setAngles(angleX, angleY, angleZ);
        reading.setStartTime(startTime);
        reading.setEndTime(endTime);

        postReadingRequest(reading);
        Log.i("reading", "" + reading.getRideId());

        startLat = endLat;
        startLng = endLng;

        startTime = endTime;

        clearArrays();
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
        Log.i("GPS startUpdates", "startLocationUpdates called");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(250);
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

    public ArrayList<Float> trimArray(ArrayList<Float> array){
        int arrayCount = array.size();
        ArrayList<Float> newArray = new ArrayList<Float>();
        if(arrayCount > 10){
            for(int i = arrayCount - 10; i < arrayCount; i ++){
                newArray.add(array.get(i));
            }
            return newArray;
        }
        else{
            return array;
        }
    }

    public void getCalibrationAndScoreboardIds(){
        calibrationId = sharedPreferences.getInt("calibration_id", 0);
        scoreboardId = sharedPreferences.getInt("scoreboard_id", 0);
        Log.i("getcalibrationandscoreboard", "calibration_id: " + calibrationId);
        Log.i("getcalibrationandscoreboard", "scoreboard_id: " + scoreboardId);
    }
    public void setCalibrationAndScoreboardIds(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(calibrationId == 0){
            calibrationId = rideId;
            editor.putInt("calibration_id", calibrationId);
            Log.i("calibration", "setCalibrationAndScoreboardIds called");
        }
        if(scoreboardId == 0){
            scoreboardId = rideId;
            editor.putInt("scoreboard_id", scoreboardId);
        }
        editor.commit();
    }

    public void updateIds(){
        getCalibrationAndScoreboardIds();
        setCalibrationAndScoreboardIds();
    }

    public void putRideRequest(int id, Ride ride){
        Log.i("putRide", "putRideRequest called");
        Call<Ride> calibrationAndScoreboardCall = pavementAPIService.putRide(id, ride);
        calibrationAndScoreboardCall.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                Log.i("putRide onResponse", "Ride: onSuccess: " + response.body() + "; onError: " + response.errorBody());
                Ride savedRide = response.body();
                int savedCalibrationId = savedRide.getCalibrationId();
                Log.i("CalibrationID", "" + savedCalibrationId);
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                Log.i("putRide onFailure", "put ride failed");
            }
        });
    }

    public void postReadingRequest(Reading reading){
        Call<Reading> call = pavementAPIService.postReading(reading);
        call.enqueue(new Callback<Reading>() {
            @Override
            public void onResponse(Call<Reading> call, Response<Reading> response) {
                Log.i("Reading onResponse", "response: onSuccess: " + response.body() + "; onError: " + response.errorBody());
            }

            @Override
            public void onFailure(Call<Reading> call, Throwable t) {
                Log.i("Reading onFailure", "Well, that didn't work");
            }
        });
    }

    public double getCurrentTime(){
        return System.currentTimeMillis()/1000;
    }


}
