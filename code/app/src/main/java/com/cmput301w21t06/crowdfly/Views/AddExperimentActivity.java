package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Handles adding an experiment
 * Yet to handle location data
 */
public class AddExperimentActivity extends AppCompatActivity {

    Button btnAddExperiment;
    Button btnCancel;
    SwitchCompat regionSwitch;
    EditText etDescription;
    EditText etMinNumTrials;
    EditText etRegion;
    Button btnMeasurement;
    Button btnBinomial;
    Button btnCount;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experiment);
        ExperimentLog experimentLog = ExperimentLog.getExperimentLog();

        //btnAddExperiment = findViewById(R.id.add_experiment);
        regionSwitch = findViewById(R.id.regionSwitch);
        btnCancel = findViewById(R.id.cancelBtn);
        //etDescription = findViewById(R.id.trial_listview);
        etMinNumTrials = findViewById(R.id.min_trial_edit_text);
        etRegion = findViewById(R.id.region_edit_text);
        etDescription = findViewById(R.id.descriptionBox);
        btnMeasurement = findViewById(R.id.m_btn);
        btnBinomial = findViewById(R.id.binomial_btn);
        btnCount = findViewById(R.id.count_btn);
        userID = FirebaseAuth.getInstance().getUid();
        //set clickers to false until user enters input
        btnCount.setEnabled(false);
        btnBinomial.setEnabled(false);
        btnMeasurement.setEnabled(false);


        etMinNumTrials.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                btnCount.setEnabled(etMinNumTrials.getText().toString().trim().length() > 0);
                btnCount.setBackgroundColor(Color.parseColor("#2B547E"));
                btnMeasurement.setEnabled(etMinNumTrials.getText().toString().trim().length() > 0);
                btnMeasurement.setBackgroundColor(Color.parseColor("#2B547E"));
                btnBinomial.setEnabled(etMinNumTrials.getText().toString().trim().length() > 0);
                btnBinomial.setBackgroundColor(Color.parseColor("#2B547E"));

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnBinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hard coding variables for now will be adjusted
                String description = getDescription();
                String region = getRegion();
                int minNumTrials = getMinNumTrials();
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"binomial");
                ExperimentController.addExperimentData(expAdd);
                finish();
            }
        });

        btnMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = getDescription();
                String region = getRegion();
                int minNumTrials = getMinNumTrials();
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"measurement");
                ExperimentController.addExperimentData(expAdd);
                finish();
            }
        });

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = getDescription();
                String region = getRegion();
                int minNumTrials = getMinNumTrials();

                Log.d("myTag", region);
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"count");
                ExperimentController.addExperimentData(expAdd);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    /**
     * Get the region from the input box
     * @return
     * Returns the region
     */
    public String getRegion(){
        return etRegion.getText().toString();
    }

    private String getDescription(){return etDescription.getText().toString();}

    /**
     * Gets the number of trials from the input box
     * @return
     * Returns the number of trials
     */
    public Integer getMinNumTrials(){
        String minNumTrials = etMinNumTrials.getText().toString();
        return Integer.parseInt(minNumTrials);
    }

    private void makeToast(String toast, int n) {
        //0 for short toast, 1 for long toast
        if (n == 0) {
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
        }

    }

}