
//class for adding new Trials

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * this is an activity that adds a new trial to the Listview in the Trial log
 */

public class NewTrialActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpListener, EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener{
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String LATITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LAT";
    private final String LONGITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LONG";
    private final int lCode = 0;
    private Double latitude,longitude;
    private EditText trialDesc, successes, failures;
    private Button region;
    private Button addButton, buttonBinomial, buttonMeasure, buttonCount, buttonCancel;
    public int newTrialSuccesses, newTrialFailures, newTrialCount;
    public double newTrialMeasurement;
    public String newTrialDescription;
    boolean forceRegion;
    public String trialType;
    public String expID;
    public Experiment exp;
    private String userID = FirebaseAuth.getInstance().getUid();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trial);

        TrialLog trialLog = TrialLog.getTrialLog();

        //instantiate variables
        buttonCancel = findViewById(R.id.cancelButton);
        region = findViewById(R.id.regionButton);
        addButton = findViewById(R.id.newTrialAddButton);
        buttonBinomial = findViewById(R.id.binTrial);
        buttonMeasure = findViewById(R.id.measureTrial);
        buttonCount = findViewById(R.id.countTrial);

        drawerLayout = findViewById(R.id.drawer_addTrial);
        navigationView = findViewById(R.id.nav_view_add_trial);
        toolbar = findViewById(R.id.toolbar_add_trial);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });


        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID,this);
        trialType = exp.getType();
        forceRegion = exp.getRegionEnabled();
        if (forceRegion){
            Toaster.makeCrispyToast(this,"Please note that the experiment creator has required location data be collected!");
        }
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
                setResult(RESULT_CANCELED, null);
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

        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dd","CLICKCED");
                Intent intent = new Intent(NewTrialActivity.this,ViewLocationActivity.class);
                intent.putExtra(SELECTION,false);
                startActivityForResult(intent,lCode);
            }
        });

        // Confirmation button, this will direct the user back to the viewTrialLogActivity Page
        // with an intent bundled with information given from the extended fragment from the newTrial
        // activity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forceRegion || (forceRegion && !String.valueOf(region.getText()).matches(""))) {
                    if (trialType.equals("binomial")) {
                        BinomialTrial trialAdd = new BinomialTrial(newTrialDescription, newTrialSuccesses, newTrialFailures, "", userID, getRegion());
                        returnTrial(trialAdd);
                    }
                    else if (trialType.equals("count")) {
                        CountTrial trialAdd = new CountTrial(newTrialDescription, newTrialCount, "", userID,getRegion());
                        returnTrial(trialAdd);
                    }
                    else if (trialType.equals("measurement")) {
                        MeasurementTrial trialAdd = new MeasurementTrial(newTrialDescription, newTrialMeasurement, "", userID,getRegion());
                        returnTrial(trialAdd);
                    }
                    else {
                        setResult(RESULT_CANCELED, null);
                    }
                    finish();
                }
                else {
                    Toaster.makeCrispyToast(NewTrialActivity.this,"Region was enforced by the experimenter creator and one has not been entered! ");
                }
            }
        });

    }

    private void returnTrial(Trial trialAdd) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("trialData", new HashMap<String,Object>(trialAdd.toHashMap()));
        setResult(RESULT_OK, returnIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == lCode){
            latitude = data.getDoubleExtra(LATITUDE,0.);
            longitude = data.getDoubleExtra(LONGITUDE,0.);
            region.setText(ViewLocationActivity.getStringLocation(latitude,longitude,true));
        }
    }

    private String getRegion(){
        String regionString = region.getText().toString();
        if (!regionString.matches("")){
            return ViewLocationActivity.getStringLocation(latitude,longitude,false);
        }
        return regionString;
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
        if (trial != null) {
            newTrialDescription = ((BinomialTrial) trial).getDescription();
            newTrialSuccesses = ((BinomialTrial) trial).getSuccesses();
            newTrialFailures = ((BinomialTrial) trial).getFailures();
            buttonBinomial.setBackgroundColor(Color.BLUE);
        }
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
        if (trial != null) {
            Log.d("NEW TRIAL", "onOkPressed CountTrial version");
            newTrialDescription = ((CountTrial) trial).getDescription();
            newTrialCount = ((CountTrial) trial).getCount();
            buttonCount.setBackgroundColor(Color.BLUE);
        }
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
        if (trial != null) {
            Log.d("NEW TRIAL", "onOkPressed MeasurementTrial Version");
            newTrialDescription = ((MeasurementTrial) trial).getDescription();
            newTrialMeasurement = ((MeasurementTrial) trial).getMeasurement();
            buttonMeasure.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        exp = experiment;
    }
}