package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * This class handles picking a location as well as showing all the locations of a particular activity
 */
public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback, Toaster {
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String EXP = "COM.CMPUT301W21T06.CROWDFLY.MAP.EXP";
    private final String LATITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LAT";
    private final String LONGITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LONG";
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private String owner;
    private Button doneButton;
    private boolean getAll;
    private HashMap<String, String[]> locations;
    private Marker marker = null;
    private GoogleMap map;

    GoogleMap.OnMapClickListener mapListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (marker != null) {
                marker.remove();
            }
            marker = map.addMarker(new MarkerOptions().position(latLng).title("Coordinates: " + getStringLocation(latLng.latitude, latLng.longitude, true)));
            Toaster.makeToast(ViewLocationActivity.this, "Marker Placed, press and hold anywhere to remove it!");
            doneButton.setBackgroundColor(Color.parseColor("#2B547E"));
            doneButton.setText(R.string.Done);
        }
    };

    GoogleMap.OnMapLongClickListener mapLongListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            if (marker != null) {
                marker.remove();
                Toaster.makeToast(ViewLocationActivity.this, "Marker removed, tap anywhere on the map to add a new one!");
                doneButton.setBackgroundColor(Color.parseColor("#E32610"));
                doneButton.setText(R.string.pickLocation);
                marker = null;
            }
        }
    };

    OnSuccessListener successListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                map.moveCamera(point);
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
                intent.putExtra(LONGITUDE, latLng.longitude);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toaster.makeToast(ViewLocationActivity.this, "Please select a location first!");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        setUp(savedInstanceState);
    }


    /**
     * This overrides what happens if the user presses the back button in the toolbar
     * @param item
     * This is what the user pressed in the toolbar
     * @return
     * This is a boolean that is not important in this case
     */
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

    private void getNeededPermissions() {
        if (!getAll) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            } else {
                Toaster.makeCrispyToast(this, "Location permissions already granted, application is currently using your location!");
                mFusedLocationClient.getLastLocation().addOnSuccessListener(successListener);
            }

        }
    }

    private void setUpViews(Bundle savedInstanceState) {
        doneButton = findViewById(R.id.doneMapButton);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        getAll = intent.getBooleanExtra(SELECTION, false);
        if (getAll) {
            locations = (HashMap<String, String[]>) intent.getSerializableExtra(EXP);
        }
    }

    private void createMarkers() {
        Double[] arr = null;
        for (String id : locations.keySet()) {
            String[] sArr = locations.get(id);
            String prefix = "Trial ";
            owner = UserController.reverseConvert(sArr[1]);
            arr = parseStringLocation(sArr[0]);
            if (sArr.length == 2) {
                prefix = "Experiment ";
            }
            String title = prefix + "coordinates: " + getStringLocation(arr[0], arr[1], true) + "; Owner: " + owner;
            map.addMarker(new MarkerOptions().position(new LatLng(arr[0], arr[1])).title(title));
        }
        if (arr != null) {
            panMap(arr);
        }
        else{
            Log.e("LocationViewer","There were no markers!");
        }
    }

    private void panMap(Double[] arr){
        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(arr[0], arr[1]));
        map.animateCamera(point);
    }

    private void handleButtonSetup() {
        if (getAll) {
            doneButton.setVisibility(View.INVISIBLE);
        } else {
            doneButton.setOnClickListener(doneListener);
        }
    }

    /**
     * This is the callback function to implement when a map object has been created
     * @param googleMap
     * This is the map object
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getNeededPermissions();
        if (!getAll) {
            map.setOnMapLongClickListener(mapLongListener);
            map.setOnMapClickListener(mapListener);
        } else {
            createMarkers();
        }
    }

    /**
     * This is the callback for permissions have been requested from a user, in our case for location services
     * @param requestCode
     * This is the particular permissions request code
     * @param permissions
     * This is the permissions we were looking for
     * @param grantResults
     * The result for each permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && ContextCompat.checkSelfPermission(ViewLocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toaster.makeToast(ViewLocationActivity.this, "Thank you! Location services enabled!");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(successListener);
        }
        else{
            Toaster.makeToast(ViewLocationActivity.this, "Location services disabled! Please manually specify a location!");
        }
    }

    /**
     * This converts a locations coordinates to strings either for storage or display
     * @param latitude
     * The latitude portion of the coordinate
     * @param longitude
     * The longitude portion of the coordiante
     * @param disp
     * This is a boolean indicating whether the returned string is for display or not
     * @return
     * This is the formatted coordinate string
     */
    public static String getStringLocation(Double latitude, Double longitude, boolean disp){
        if (!disp) {
            return (latitude + "," + longitude);
        }
        else{
            return (String.format("%.4f",latitude) + ", " + String.format("%.4f",longitude));
        }
    }

    /**
     * This converts a database string back to a pair of doubles
     * @param loc
     * This is the database string
     * @return
     * This is an array of type Double holding the coordinates
     */
    public static Double[] parseStringLocation(String loc){
        String arr[] = loc.split(",");
        Double latitude = Double.parseDouble(arr[0]);
        Double longitude = Double.parseDouble(arr[1]);
        Double dArr[] = {latitude,longitude};
        return dArr;
    }
}