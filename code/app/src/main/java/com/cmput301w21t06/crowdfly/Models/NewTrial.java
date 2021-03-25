
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

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.R;
import com.cmput301w21t06.crowdfly.Views.EditBinomialTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditCountTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditMeasureTrialFragment;
import com.cmput301w21t06.crowdfly.Views.ViewTrialLogActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * this is an activity that adds a new trial to the Listview in the Trial log
 */

public class NewTrial extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpListener, EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener{

    private EditText regionEnforced, trialDesc, regionType,  successes, failures;
    private Button addButton, buttonBinomial, buttonMeasure, buttonCount, buttonCancel;
    public int newTrialSuccesses, newTrialFailures, newTrialCount;
    public double newTrialMeasurement;
    public String newTrialDescription;

    public String trialType;
    public String expID;
    public Experiment exp;
    private String userID = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trial);

        TrialLog trialLog = TrialLog.getTrialLog();

        //instantiate variables
        buttonCancel = findViewById(R.id.cancelButton);
        regionEnforced = findViewById(R.id.regionEnforcedEditText);
        regionType = findViewById(R.id.regionTypeEditText);
        addButton = findViewById(R.id.newTrialAddButton);
        buttonBinomial = findViewById(R.id.binTrial);
        buttonMeasure = findViewById(R.id.measureTrial);
        buttonCount = findViewById(R.id.countTrial);


        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID,this);
        trialType = exp.getDescription();
        Log.e("ddff",expID);
        Log.e("Ddd",trialType+  " " + exp.getDescription());
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
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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



                if(trialType.equals("binomial")){
                    BinomialTrial trialAdd = new BinomialTrial(newTrialDescription, newTrialSuccesses, newTrialFailures, "", userID);
                    exp.getTrialController().addTrialData(trialAdd, expID);
                }
                if(trialType.equals("count")){
                    CountTrial trialAdd = new CountTrial(newTrialDescription, newTrialCount,"", userID);
                    exp.getTrialController().addTrialData(trialAdd, expID);
                }
                if(trialType.equals("measurement")){
                    MeasurementTrial trialAdd = new MeasurementTrial(newTrialDescription, newTrialMeasurement, "", userID);
                    exp.getTrialController().addTrialData(trialAdd, expID);
                }
                Intent intent = new Intent(getApplicationContext(), ViewTrialLogActivity.class);
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
        newTrialCount = ((CountTrial) trial).getCount();
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
        newTrialMeasurement = ((MeasurementTrial) trial).getMeasurement();
        buttonMeasure.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        exp = experiment;
    }
}