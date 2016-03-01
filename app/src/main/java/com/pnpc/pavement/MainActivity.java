package com.pnpc.pavement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int LOCATION_REQUEST = 1337;
    Button serviceButton;
    private TextView locationView;
    private TextView lngView;
    private TextView latView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        locationView=(TextView)findViewById(R.id.location_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("GPS permission code", "Check self permission: " + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        }
        updateLocationView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(hasLocationPermission() == false) {
                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            }
        }
        serviceButton = (Button) findViewById(R.id.service_button);

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, PavementService.class);
                if (serviceButton.getText() == "Start Service") {
                    startService(serviceIntent);
                    serviceButton.setText("Stop Service");
                    Log.d("", "startService called");
                } else {
                    stopService(serviceIntent);
                    serviceButton.setText("Start Service");
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
        if (id == R.id.action_settings) {
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
                updateLocationView();
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
    private void updateLocationView(){
        locationView.setText(String.valueOf(hasLocationPermission()));
    }

}
