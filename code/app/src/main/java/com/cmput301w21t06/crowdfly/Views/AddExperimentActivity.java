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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String LATITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LAT";
    private final String LONGITUDE = "COM.CMPUT301W21T06.CROWDFLY.MAP.LONG";
    private final int lCode = 0;
    Button btnCancel;
    SwitchCompat regionSwitch;
    EditText etDescription;
    EditText etMinNumTrials;
    TextView etRegion;
    Button btnMeasurement;
    Button btnBinomial;
    Button btnCount;
    String userID;
    Double latitude;
    Double longitude;
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("tag","d" + String.valueOf(canEnable()));
                handleButton();

            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        regionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                handleButton();
                if (isOn == true){
                    Toaster.makeCrispyToast(AddExperimentActivity.this,"Region enabled!");
                }
            }
        });

        btnBinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hard coding variables for now will be adjusted
                String description = getDescription();
                String region = getRegion();
                int minNumTrials = getMinNumTrials();
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"binomial",regionSwitch.isChecked());
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
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"measurement",regionSwitch.isChecked());
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
                Experiment expAdd = new Experiment(description, region, minNumTrials,userID,"count",regionSwitch.isChecked());
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

        etRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dd","CLICKCED");
                Intent intent = new Intent(AddExperimentActivity.this,ViewLocationActivity.class);
                intent.putExtra(SELECTION,false);
                startActivityForResult(intent,lCode);
            }
        });
    }

    /**
     * Get the region from the input box
     * @return
     * Returns the region
     */
    private String getRegion(){
        String region = etRegion.getText().toString();
        if (!region.matches("")){
            return ViewLocationActivity.getStringLocation(latitude,longitude,false);
        }
        return region;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == lCode){
            latitude = data.getDoubleExtra(LATITUDE,0.);
            longitude = data.getDoubleExtra(LONGITUDE,0.);
            etRegion.setText(ViewLocationActivity.getStringLocation(latitude,longitude,true));
            handleButton();
        }
    }

    private boolean canEnable(){
        Log.e("dd",String.valueOf(!etMinNumTrials.getText().toString().matches("")));
        boolean condition = (!regionSwitch.isChecked() || (regionSwitch.isChecked() && !(String.valueOf(etRegion.getText())).matches("")));
        return (condition && (!etMinNumTrials.getText().toString().matches("")));
    }

    private void handleButton(){
        btnCount.setEnabled(canEnable());
        btnMeasurement.setEnabled(canEnable());
        btnBinomial.setEnabled(canEnable());

        if (canEnable()){
            btnCount.setBackgroundColor(Color.parseColor("#2B547E"));
            btnMeasurement.setBackgroundColor(Color.parseColor("#2B547E"));
            btnBinomial.setBackgroundColor(Color.parseColor("#2B547E"));
        }
        else{
            Log.e("doing this","ddddf");
            btnCount.setBackgroundColor(getResources().getColor(R.color.lightbluejay));
            btnMeasurement.setBackgroundColor(getResources().getColor(R.color.lightbluejay));
            btnBinomial.setBackgroundColor(getResources().getColor(R.color.lightbluejay));
        }
    }

}