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
    private Button registerCode;
    private Button generateCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_q_r);
        scanCode = findViewById(R.id.scanQRButton);
        registerCode = findViewById(R.id.registerBarcodeButton);
        generateCode = findViewById(R.id.generateQRButton);
        setup();
    }

    private void setup(){
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQRActivity.this, ScanCodeActivity.class);
                startActivity(intent);
            }
        });
        registerCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQRActivity.this, RegisterBarcodeActivity.class);
                startActivity(intent);
            }
        });
        generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQRActivity.this, GenerateQRActivity.class);
                startActivity(intent);
            }
        });
    }

}