package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity bridges various activites together and provides the starting screen for the user
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    private final String userID = FirebaseAuth.getInstance().getUid();
    private Button viewExperimentLogButton;
    private Button userSearch;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dummySetup();
        //instantiating the hamburger menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.userFBID);
        //UserController.getUserProfile(UserController.reverseConvert(FirebaseAuth.getInstance().getUid()), this);
        navUserId.setText(userID);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        viewExperimentLogButton = (Button) findViewById(R.id.viewExperimentLogBtn);
        userSearch = findViewById(R.id.userSearchButton);

        viewExperimentLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewExperimentLogActivity.class);
                startActivity(intent);
            }
        });

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, searchUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void dummySetup(){
        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap){}
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.hamHome:
                //Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent1);
                break;
            case R.id.hamAccount:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra(TAG, UserController.reverseConvert(userID));
                startActivity(intent);
                break;
            case R.id.hamExperiment:
                drawerLayout.closeDrawers();
                Intent intent2 = new Intent(MainActivity.this, ViewExperimentLogActivity.class);
                startActivity(intent2);
                break;

        }
        return true;
    }
}