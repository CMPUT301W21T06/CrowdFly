package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.maps.MapView;

public class ViewLocationActivity extends AppCompatActivity {
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String EXP = "COM.CMPUT301W21T06.CROWDFLY.MAP.EXP";
    MapView map;
    Button doneButton;
    Button cancelButton;
    boolean getAll;
    Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        setUp();
    }

    private void setUp(){
        receiveIntent();
        setUpViews();
        handleLook();
    }
    private void setUpViews(){
        map = findViewById(R.id.mapView);
        doneButton = findViewById(R.id.doneMapButton);
        cancelButton = findViewById(R.id.cancelMapButton);
    }

    private void receiveIntent(){
        Intent intent = getIntent();
        getAll = intent.getBooleanExtra(SELECTION, false);
        if (getAll){
            experiment = (Experiment) intent.getSerializableExtra(EXP);
        }
    }

    private void handleLook(){
        if (getAll){
            cancelButton.setVisibility(View.INVISIBLE);
            doneButton.setWidth(map.getWidth());
        }
    }
}