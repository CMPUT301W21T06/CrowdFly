package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback, Toaster {
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String EXP = "COM.CMPUT301W21T06.CROWDFLY.MAP.EXP";
    private final String LATITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LAT";
    private final String LONGITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LONG";
    private Button doneButton;
    private boolean getAll;
    private HashMap<String,String> locations;
    private Marker marker = null;
    private GoogleMap map;

    GoogleMap.OnMapClickListener mapListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (marker != null){
                marker.remove();
            }
            marker = map.addMarker(new MarkerOptions().position(latLng).title("Coordinates: " + getStringLocation(latLng.latitude,latLng.longitude,true)));
            Toaster.makeToast(ViewLocationActivity.this,"Marker Placed, press and hold anywhere to remove it!");
            doneButton.setBackgroundColor(Color.parseColor("#2B547E"));
            doneButton.setText(R.string.Done);
        }
    };

    GoogleMap.OnMapLongClickListener mapLongListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            if (marker != null){
                marker.remove();
                Toaster.makeToast(ViewLocationActivity.this, "Marker removed, tap anywhere on the map to add a new one!");
                doneButton.setBackgroundColor(Color.parseColor("#E32610"));
                doneButton.setText(R.string.pickLocation);
                marker = null;
            }
        }
    };

    View.OnClickListener doneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (marker != null) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent();
                intent.putExtra(LATITUDE, latLng.latitude);
                intent.putExtra(LONGITUDE,latLng.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
            else{
                Toaster.makeToast(ViewLocationActivity.this,"Please select a location first!");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        setUp(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUp(Bundle savedInstanceState) {
        receiveIntent();
        setUpViews(savedInstanceState);
        handleButtonSetup();
    }

    private void setUpViews(Bundle savedInstanceState) {
        doneButton = findViewById(R.id.doneMapButton);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        getAll = intent.getBooleanExtra(SELECTION, false);
        if (getAll) {
            locations = (HashMap<String,String>) intent.getSerializableExtra(EXP);
        }
    }

    private void createMarkers(){
        for (String id : locations.keySet()){
            String loc = locations.get(id);
            Double[] arr = parseStringLocation(loc);
            map.addMarker(new MarkerOptions().position(new LatLng(arr[0],arr[1])).title(id));
        }
    }

    private void handleButtonSetup(){
        if (getAll){
            doneButton.setVisibility(View.INVISIBLE);
        }
        else{
            doneButton.setOnClickListener(doneListener);
        }
    }

    @Override
    public void onMapReady(@NonNull  GoogleMap googleMap) {
        map = googleMap;
        if (!getAll) {
            map.setOnMapLongClickListener(mapLongListener);
            map.setOnMapClickListener(mapListener);
        }
        else{
            createMarkers();
        }
    }


    public static String getStringLocation(Double latitude, Double longitude, boolean disp){
        if (!disp) {
            return (latitude + "," + longitude);
        }
        else{
            return (String.format("%.4f",latitude) + ", " + String.format("%.4f",longitude));
        }
    }

    public static Double[] parseStringLocation(String loc){
        String arr[] = loc.split(",");
        Double latitude = Double.parseDouble(arr[0]);
        Double longitude = Double.parseDouble(arr[1]);
        Double dArr[] = {latitude,longitude};
        return dArr;
    }
}