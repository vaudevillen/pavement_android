package com.pnpc.pavement;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;

/**
 * Created by jjshin on 2/21/16.
 */
public class PavementService extends Service implements LocationListener, SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    SensorManager sensorManager;
    LocationManager locManager;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    Boolean clientConnected = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
        setupLocationRequest();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


    public void setupLocationRequest() {
        Log.i("GPS Permission", "" + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        Log.i("GPS Permission result", "" + PackageManager.PERMISSION_GRANTED);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//
        } else {
            if (clientConnected == true){
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        googleApiClient);
                if (lastLocation != null) {
                    Log.i("GPS onConnected", "Latitude: " + lastLocation.getLatitude());
                    Log.i("GPS onConnected", "Longitude: " + lastLocation.getLongitude());
                }
                else{
                    locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    Log.i("GPS Test", "GPS Enabled: " + locManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                }
            }
        }

        return;

    }

    void displayLocationData(Location location) {
        if (location == null) {
            Log.i("GPS", "Location null");
            return;
        }

        Log.i("GPS", "Latitude: " + location.getLatitude());
        Log.i("GPS", "Longitude: = " + location.getLongitude());
        Log.i("GPS", "Time: " + location.getTime());
        if (location.hasAccuracy()) {
            Log.i("GPS", "Accuracy: " + location.getAccuracy());
        }
        if (location.hasSpeed()) {
            Log.i("GPS", "Speed: " + location.hasSpeed());
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i("Sensor Test", "SensorUpdate: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("GPS", "provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("GPS", "provider disaabled");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("GPS googleclient", "GoogleClientApi connected");
        clientConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GPS googleclient", "GoogleClientApi connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("GPS googleclient", "GoogleClientApi connection failed");

    }
}
