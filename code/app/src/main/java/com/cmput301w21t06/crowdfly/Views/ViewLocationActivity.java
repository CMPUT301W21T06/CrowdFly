package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.maps.MapView;

public class ViewLocationActivity extends AppCompatActivity {
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.ALL";
    MapView map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        setUpViews();
    }


    public void setUpViews(){
        map = findViewById(R.id.mapView);

    }
}