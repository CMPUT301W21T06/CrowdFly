
//class for adding new Trials

package com.cmput301w21t06.crowdfly.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301w21t06.crowdfly.R;
import com.cmput301w21t06.crowdfly.Views.EditBinomialTrialFragment;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;

public class NewTrial extends AppCompatActivity {

    private EditText regionEnforced, trialDesc, regionType,  successes, failures;
    private Button addButton, buttonS, buttonF, buttonBinomial, buttonMeasure, buttonCount;
    public int current_successes = 0, current_failures = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trial);

        //instantiate variables
        regionEnforced = findViewById(R.id.regionEnforcedEditText);
        trialDesc = findViewById(R.id.trialDesc);
        regionType = findViewById(R.id.regionTypeEditText);
        successes = findViewById(R.id.countSuccesses);
        failures = findViewById(R.id.countFailures);
        addButton = findViewById(R.id.newTrialAddButton);
        buttonBinomial = findViewById(R.id.binTrial);
        buttonMeasure = findViewById(R.id.measureTrial);
        buttonCount = findViewById(R.id.countTrial);

        buttonS = findViewById(R.id.successbutton);
        buttonF = findViewById(R.id.failurebutton);
        successes.setText("0");
        failures.setText("0");

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_successes +=1;
                successes.setText(String.valueOf(current_successes));
            }
        });

        buttonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_failures +=1;
                failures.setText(String.valueOf(current_failures));
            }
        });

        buttonBinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBinomialTrialFragment editBinomialTrialFragment = new EditBinomialTrialFragment();
                editBinomialTrialFragment.show(getSupportFragmentManager(), "EDIT TEXT");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewTrialLogActivity.class);
                intent.putExtra("trialDesc", trialDesc.getText().toString());
                intent.putExtra("success",successes.getText().toString());
                intent.putExtra("failure",failures.getText().toString());
                startActivity(intent);
            }
        });


    }
}