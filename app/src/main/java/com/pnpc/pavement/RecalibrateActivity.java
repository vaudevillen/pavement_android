package com.pnpc.pavement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jjshin on 3/21/16.
 */
public class RecalibrateActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView recalibrateButton;
    SharedPreferences sharedPreferences;
    boolean serviceStarted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recalibrate);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        serviceStarted = bundle.getBoolean("serviceStarted");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        float milesMeasured = sharedPreferences.getFloat("miles_measured", 0);
        String formattedMiles = String.format("%.02f", milesMeasured);

        TextView milesText = (TextView) findViewById(R.id.miles_measured);
        milesText.setText(formattedMiles);

        recalibrateButton = (ImageView) findViewById(R.id.recalibrate_button);
        recalibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.remove("calibration_id");
                editor.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Spannable actionBarTitle = new SpannableString("Pavement");
        actionBarTitle.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0,
                actionBarTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(actionBarTitle);

        Drawable pavementIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.pavement_tab, null);
        pavementIcon.setColorFilter(null);
        Drawable statsIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.stats_tab, null);
        statsIcon.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pavement) {
            Intent intent = new Intent(RecalibrateActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("serviceStarted", serviceStarted);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
