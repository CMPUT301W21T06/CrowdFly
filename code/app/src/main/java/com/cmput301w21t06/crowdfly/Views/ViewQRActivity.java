package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;

public class ViewQRActivity extends AppCompatActivity {
    private Button scanCode;
    private Button generateCode;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_q_r);
        drawerLayout = findViewById(R.id.drawer_qr);
        navigationView = findViewById(R.id.nav_view_qr);
        toolbar = findViewById(R.id.toolbar_qr);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        scanCode = findViewById(R.id.scanQRButton);
        generateCode = findViewById(R.id.generateQRButton);
        setup();
    }

    /**
     * Setup click listeners
     */
    private void setup(){
        String experimentID = getIntent().getStringExtra("expID");
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQRActivity.this, ScanCodeActivity.class);
                addExperimentID(intent, experimentID);
                startActivity(intent);
            }
        });
        generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQRActivity.this, GenerateQRActivity.class);
                addExperimentID(intent, experimentID);
                startActivity(intent);
            }
        });
    }

    /**
     * Pass in experiment ID to an intent
     * @param intent
     * @param experimentID
     */
    private void addExperimentID(Intent intent, String experimentID) {
        intent.putExtra("experimentID", experimentID);
    }

}