package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

public class AddExperimentActivity extends AppCompatActivity {

    Button btnAddExperiment;
    Button btnCancel;
    EditText etDescription;
    EditText etMinNumTrials;
    EditText etRegion;

    Button btnMeasurement;
    Button btnBinomial;
    Button btnCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experiment);
        ExperimentLog experimentLog = ExperimentLog.getExperimentLog();

        //btnAddExperiment = findViewById(R.id.add_experiment);
        btnCancel = findViewById(R.id.cancel_btn);
        //etDescription = findViewById(R.id.trial_listview);
        etMinNumTrials = findViewById(R.id.min_trial_edit_text);
        etRegion = findViewById(R.id.region_edit_text);

        btnMeasurement = findViewById(R.id.m_btn);
        btnBinomial = findViewById(R.id.binomial_btn);
        btnCount = findViewById(R.id.count_btn);

//        btnAddExperiment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // need to add owner name later when we have users
//
//                final String description = etDescription.getText().toString();
//                final String minNumTrials = etMinNumTrials.getText().toString();
//                final String region = etRegion.getText().toString();
//
//                experimentLog.addExperiment(new Experiment(description, region, Integer.parseInt(minNumTrials)));
//
//                startActivity(new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class));
//            }
//        });

        btnBinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hard coding variables for now will be adjusted
                final String description = "binomial";
                String region = getRegion();
                int minNumTrials = getMinNumTrials();
                Experiment expAdd = new Experiment(description, region, minNumTrials);
                experimentLog.addExperiment(new Experiment(description,region,minNumTrials));
                new CrowdFlyFirestore().setExperimentData(expAdd);
                Intent intent = new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class);
                intent.putExtra("type",description);
                startActivity(intent);
            }
        });

        btnMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = "measurement";
                String region = getRegion();
                int minNumTrials = getMinNumTrials();
                Experiment expAdd = new Experiment(description, region, minNumTrials);
                experimentLog.addExperiment(new Experiment(description,region,minNumTrials));
                new CrowdFlyFirestore().setExperimentData(expAdd);
                Intent intent = new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class);
                intent.putExtra("type",description);
                startActivity(intent);
            }
        });

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = "count";
                String region = getRegion();
                int minNumTrials = getMinNumTrials();

                Log.d("myTag", region);

                if (region.equals("")){
                    makeToast("no",0);
                }
                else {
                    Experiment expAdd = new Experiment(description, region, minNumTrials);
                    experimentLog.addExperiment(new Experiment(description, region, minNumTrials));
                    new CrowdFlyFirestore().setExperimentData(expAdd);
                    Intent intent = new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class);
                    //intent.putExtra("trialType",description);
                    //Log.e("type in add experiment", description);
                    startActivity(intent);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddExperimentActivity.this, ViewExperimentLogActivity.class));
            }
        });
    }

    public String getRegion(){
        return etRegion.getText().toString();
    }
    public Integer getMinNumTrials(){
        String minNumTrials = etMinNumTrials.getText().toString();
        return Integer.parseInt(minNumTrials);
    }
    public void makeToast(String toast, int n) {
        //0 for short toast, 1 for long toast
        if (n == 0) {
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        }

    }

}