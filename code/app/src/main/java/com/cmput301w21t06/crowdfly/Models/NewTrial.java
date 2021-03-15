
//class for adding new Trials

package com.cmput301w21t06.crowdfly.Models;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.R;
import com.cmput301w21t06.crowdfly.Views.EditBinomialTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditCountTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditMeasureTrialFragment;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
/**
 * this is an activity that adds a new activity to the Listview in the Trial log
 */

public class NewTrial extends AppCompatActivity implements EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener{

    private EditText regionEnforced, trialDesc, regionType,  successes, failures;
    private Button addButton, buttonBinomial, buttonMeasure, buttonCount;
    public String newTrialSuccesses, newTrialFailures, newTrialCount, newTrialMeasurement, newTrialDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trial);

        //instantiate variables
        regionEnforced = findViewById(R.id.regionEnforcedEditText);
        regionType = findViewById(R.id.regionTypeEditText);
        addButton = findViewById(R.id.newTrialAddButton);
        buttonBinomial = findViewById(R.id.binTrial);
        buttonMeasure = findViewById(R.id.measureTrial);
        buttonCount = findViewById(R.id.countTrial);


        String trialType = getIntent().getStringExtra("trialtype");
        //condition for trial type differentiation
        if (trialType.equals("binomial")){
            buttonMeasure.setBackgroundColor(Color.LTGRAY);
            buttonMeasure.setEnabled(false);
            buttonCount.setBackgroundColor(Color.LTGRAY);
            buttonCount.setEnabled(false);
        }
        if (trialType.equals("count")){
            buttonMeasure.setBackgroundColor(Color.LTGRAY);
            buttonMeasure.setEnabled(false);
            buttonBinomial.setBackgroundColor(Color.LTGRAY);
            buttonBinomial.setEnabled(false);
        }
        if (trialType.equals("measurement")){
            buttonBinomial.setBackgroundColor(Color.LTGRAY);
            buttonBinomial.setEnabled(false);
            buttonCount.setBackgroundColor(Color.LTGRAY);
            buttonCount.setEnabled(false);
        }


        buttonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditMeasureTrialFragment editMeasureTrialFragment  = new EditMeasureTrialFragment();
                editMeasureTrialFragment.show(getSupportFragmentManager(),"EDIT_TEXT");
            }
        });

        buttonCount.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCountTrialFragment editCountTrialFragment = new EditCountTrialFragment();
                editCountTrialFragment.show(getSupportFragmentManager(),"EDIT TEXT");
            }
        }));
        buttonBinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBinomialTrialFragment editBinomialTrialFragment = new EditBinomialTrialFragment();
                editBinomialTrialFragment.show(getSupportFragmentManager(), "EDIT TEXT");
            }
        });

        // Confirmation button, this will direct the user back to the viewTrialLogActivity Page
        // with an intent bundled with information given from the extended fragment from the newTrial
        // activity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ViewTrialLogActivity.class);
                intent.putExtra("trialDesc", newTrialDescription);
                intent.putExtra("measurement", newTrialMeasurement);
                intent.putExtra("count", newTrialCount);
                intent.putExtra("success",newTrialSuccesses);
                intent.putExtra("failure",newTrialFailures);
                startActivity(intent);
            }
        });

    }

    /**
     * this method indicates that the fragment has been executed and stores information in that
     * will be passed to view trial log
     * @param trial
     *     this is the trial that is being added to the view trial log
     */
    // BinomialTrial onOkPressed
    @Override
    public void onOkPressed(BinomialTrial trial) {
        Log.d("NEW TRIAL", "onOkPressed BinomialTrial version");
        newTrialDescription = ((BinomialTrial) trial).getDescription();
        newTrialSuccesses = ((BinomialTrial) trial).getSuccesses();
        newTrialFailures = ((BinomialTrial) trial).getFailures();
        buttonBinomial.setBackgroundColor(Color.BLUE);
    }

    /**
     * this method indicates that the fragment has been executed and stores information in that
     * will be passed to view trial log
     * @param trial
     *     this is the trial that is being added to the view trial log
     */
    // CountTrial onOkPressed
    @Override
    public void onOkPressed(CountTrial trial) {
        Log.d("NEW TRIAL","onOkPressed CountTrial version");
        newTrialDescription = ((CountTrial) trial).getDescription();
        newTrialCount = String.valueOf(((CountTrial) trial).getCount());
        buttonCount.setBackgroundColor(Color.BLUE);
    }

    /**
     * this method indicates that the fragment has been executed and stores information in that
     * will be passed to view trial log
     * @param trial
     *     this is the trial that is being added to the view trial log
     */
    // Measurement onOkPressed
    @Override
    public void onOkPressed(MeasurementTrial trial) {
        Log.d("NEW TRIAL","onOkPressed MeasurementTrial Version");
        newTrialDescription = ((MeasurementTrial) trial).getDescription();
        newTrialMeasurement = String.valueOf(((MeasurementTrial)trial).getMeasurement());
        buttonMeasure.setBackgroundColor(Color.BLUE);
    }
}