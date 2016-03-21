package com.pnpc.pavement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int LOCATION_REQUEST = 1337;
    ImageView serviceButton;
    boolean serviceStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            Log.i("savedInstance", "savedInstanceState not null");
            Log.i("savedInstance", "serviceStarted: " + savedInstanceState.getBoolean("serviceStarted"));
            serviceStarted = savedInstanceState.getBoolean("serviceStarted");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("GPS permission code", "Check self permission: " + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        }

        //Checking SDK. If above 6.0, checks location permission. If not granted, requests it.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(hasLocationPermission() == false) {
                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            }
        }
        serviceButton = (ImageView) findViewById(R.id.service_button);

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, PavementService.class);
                if (serviceStarted == false) {
                    startService(serviceIntent);
                    serviceStarted = true;
                    setServiceButtonImage();
                    Log.d("PavementService", "startService called");
                } else {
                    stopService(serviceIntent);
                    serviceStarted = false;
                    setServiceButtonImage();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_stats) {
            Intent intent = new Intent(MainActivity.this, RecalibrateActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST){
            if(hasLocationPermission()){
            }
        }
    }
    private boolean hasLocationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        }
        else{
            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(serviceStarted == true){
            outState.putBoolean("serviceStarted", serviceStarted);
        }
        Log.i("onSavedInstanceState", "outstate: serviceStarted " + serviceStarted);
        super.onSaveInstanceState(outState);
    }

    public void setServiceButtonImage(){
        if(serviceStarted == false){
            serviceButton.setImageResource(R.drawable.toggle);
        }
        else{
            serviceButton.setImageResource(R.drawable.stop);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        serviceStarted = savedInstanceState.getBoolean("serviceStarted");
        if(serviceStarted == true){
            setServiceButtonImage();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
