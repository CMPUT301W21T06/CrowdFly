package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmput301w21t06.crowdfly.R;

public class ViewQRActivity extends AppCompatActivity {
    private Button scanCode;
    private Button generateCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_q_r);
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