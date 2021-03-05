package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

public class AddExperimentActivity extends AppCompatActivity {

    Button btnAddExperiment;
    Button btnCancel;
    EditText etDescription;
    EditText etMinNumTrials;
    EditText etRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experiment);
        ExperimentLog experimentLog = ExperimentLog.getExperimentLog();

        btnAddExperiment = findViewById(R.id.add_experiment);
        btnCancel = findViewById(R.id.cancel_add);
        etDescription = findViewById(R.id.add_description);
        etMinNumTrials = findViewById(R.id.add_minTrials);
        etRegion = findViewById(R.id.add_region);

        btnAddExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to add owner name later when we have users

                final String description = etDescription.getText().toString();
                final String minNumTrials = etMinNumTrials.getText().toString();
                final String region = etRegion.getText().toString();

                experimentLog.addExperiment(new Experiment(description, region, Integer.parseInt(minNumTrials)));

                startActivity(new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class));
            }
        });
    }
}