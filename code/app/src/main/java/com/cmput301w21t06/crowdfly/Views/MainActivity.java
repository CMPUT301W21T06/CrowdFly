package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cmput301w21t06.crowdfly.R;
import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;

public class MainActivity extends AppCompatActivity {


    //my name is Haris :p
    // my name is Abdullah

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ViewExperimentLogActivity.class);
        startActivity(intent);
    }
}