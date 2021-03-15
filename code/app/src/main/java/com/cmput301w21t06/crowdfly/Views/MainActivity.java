package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301w21t06.crowdfly.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button userProfileButton = (Button) findViewById(R.id.userProfileBtn);
        Button viewExperimentLogButton = (Button) findViewById(R.id.viewExperimentLogBtn);


        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        viewExperimentLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewExperimentLogActivity.class);
                startActivity(intent);
            }
        });
    }
}