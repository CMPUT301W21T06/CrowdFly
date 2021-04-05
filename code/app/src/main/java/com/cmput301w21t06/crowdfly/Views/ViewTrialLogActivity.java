
//this class displays trials within an experiment and has other functionalities

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.DropdownAdapter;
import com.cmput301w21t06.crowdfly.Controllers.TrialAdapter;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * This shows all the trials related to the particular experiment
 */
public class ViewTrialLogActivity extends AppCompatActivity implements
        EditBinomialTrialFragment.OnFragmentInteractionListener,
        EditCountTrialFragment.OnFragmentInteractionListener,
        EditMeasureTrialFragment.OnFragmentInteractionListener,
        CrowdFlyListeners.OnDoneGetTrialsListener,
        CrowdFlyListeners.OnDoneGetSubscribedListener,
        CrowdFlyListeners.OnDoneGetExpListener,
        CrowdFlyListeners.OnDoneGetUserListener,
        CrowdFlyListeners.OnDoneGetTrialListener,
        CrowdFlyListeners.OnDoneGetExperimenterIdsListener,
        Toaster
{
    public static final String EXPERIMENT_IS_NO_LONGER_ACTIVE = "This experiment is no longer active.";
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button addButton;
    private Button questionButton;
    private Button qrButton;
    private Button subButton;
    private Button endButton;
    private Spinner dropdown;
    private DropdownAdapter dropAdapter;
    static int entry_pos;
    public TrialAdapter adapter;
    static public String trialType;
    static public String expID;
    private TrialLog trialLog;
    private Boolean subscribed = false;
    private Experiment currentExperiment;
    private User currentUser;
    private Boolean isOwner = false;
    Trial reviewedTrial;
    ArrayList<String> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);
        filters = new ArrayList<String>();
        endButton = findViewById(R.id.endButton);
        dropdown = findViewById(R.id.dropDown);
        //only update the trialtype once per experiment
        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID, this);
        trialType = currentExperiment.getType();
        trialLog = TrialLog.getTrialLog();
        setUpList();
        UserController.getUserProfile(UserController.reverseConvert(FirebaseAuth.getInstance().getUid()), this);
        //setup the data
        setupData();

        trialArrayList = trialLog.getTrials();

        questionButton = findViewById(R.id.questionButton);

        currentExperiment.getTrialController().getExperimenterIds(this);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                String text = String.valueOf(textView.getText());

                if (dropAdapter.addSelectedPosition(i)) {
                    filters.add(text);
                }
                else{
                    filters.remove(text);
                }
                dropdown.setSelection(0);
                currentExperiment.getTrialController().getTrialLogData(ViewTrialLogActivity.this, filters);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        qrButton = findViewById(R.id.QRButton);
        addButton = findViewById(R.id.addButton);
        questionButton = findViewById(R.id.questionButton);
        subButton = findViewById(R.id.subButton);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentExperiment!=null && currentUser != null){
                    if( !subscribed){
                        currentExperiment.subscribe(currentUser);
                        currentExperiment.isSubscribed(currentUser, ViewTrialLogActivity.this);
                    }
                    else {
                        currentExperiment.unsubscribe(currentUser);
                        currentExperiment.isSubscribed(currentUser, ViewTrialLogActivity.this);
                    }
                }
            }
        });
        endButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentExperiment!=null && currentUser != null){
                    if(isOwner){
                        if(!currentExperiment.getStillRunning()){
                            currentExperiment.setStillRunning(true);
                            endButton.setText("Start");
                        }
                        else {
                            if (currentExperiment.canEnd()) {
                                currentExperiment.setStillRunning(false);
                                endButton.setText("End");
                            }
                            else{
                                Toaster.makeToast(ViewTrialLogActivity.this,"The minimum number of trials have not yet been achieved!");
                            }
                        }
                        ExperimentController.setExperimentData(currentExperiment);
                    }
                    else {
                        Log.e("rr","run");
                        Toaster.makeToast(ViewTrialLogActivity.this, "Only the owner can end an experiment!");
                    }
                }
            }
        });
        questionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(subscribed || isOwner){
                    Intent intent = new Intent(ViewTrialLogActivity.this, ViewQuestionLogActivity.class);
                    intent.putExtra("expID", String.valueOf(expID));
                    startActivity(intent);
                }
                else {
                    Toaster.makeToast(ViewTrialLogActivity.this, "Please subscribe to the experiment to add trials");
                }
            }
        });


        //add trials
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
                    return;
                }
                if(subscribed || isOwner){

                    Intent intent = new Intent(getApplicationContext(), NewTrialActivity.class);
                    intent.putExtra("trialType", trialType);
                    intent.putExtra("expID", String.valueOf(expID));
                    startActivityForResult(intent,0);
                }
                else {
                    Toaster.makeToast(ViewTrialLogActivity.this, "Please subscribe to the experiment to add trials");
                }

            }
        });



        //delete trials
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
                    return false;
                }
                if(subscribed || isOwner) {
                    Trial deleteTrial = (Trial) parent.getAdapter().getItem(position);
                    String trialIDAtPos = deleteTrial.getTrialID();
                    currentExperiment.getTrialController().removeTrialData(trialIDAtPos);
                    currentExperiment.getTrialController().getTrialLogData(ViewTrialLogActivity.this, filters);
                    changeVisibility();
                }
                else {
                    Toaster.makeToast(ViewTrialLogActivity.this,"Please subscribe to the experiment to remove trials");
                }
                return false;
            }
        });

        //edit trials
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
                    return;
                }
                if(subscribed || isOwner) {

                    Trial x = trialLog.getTrial(i);
                    if (trialType.equals("binomial")) {
                        EditBinomialTrialFragment editBinomialTrialFragment = new EditBinomialTrialFragment();
                        entry_pos = i;
                        Trial btrial = (Trial) adapterView.getAdapter().getItem(i);
                        String trialIDAtPos = btrial.getTrialID();
                        currentExperiment.getTrialController().getTrial(trialIDAtPos, ViewTrialLogActivity.this);
                        editBinomialTrialFragment.newInstance((BinomialTrial) reviewedTrial).show(getSupportFragmentManager(), "EDIT TEXT");

                    }
                    if (trialType.equals("count")) {
                        EditCountTrialFragment editCountTrialFragment = new EditCountTrialFragment();
                        entry_pos = i;

                        Trial ctrial = (Trial) adapterView.getAdapter().getItem(i);
                        String trialIDAtPos = ctrial.getTrialID();
                        currentExperiment.getTrialController().getTrial(trialIDAtPos, ViewTrialLogActivity.this);
                        editCountTrialFragment.newInstance((CountTrial) reviewedTrial).show(getSupportFragmentManager(), "EDIT TEXT");

                    }
                    if (trialType.equals("measurement")) {
                        EditMeasureTrialFragment editMeasureTrialFragment = new EditMeasureTrialFragment();
                        entry_pos = i;
                        Trial mtrial = (Trial) adapterView.getAdapter().getItem(i);
                        String trialIDAtPos = mtrial.getTrialID();
                        currentExperiment.getTrialController().getTrial(trialIDAtPos, ViewTrialLogActivity.this);
                        editMeasureTrialFragment.newInstance((MeasurementTrial) reviewedTrial).show(getSupportFragmentManager(), "EDIT TEXT");

                    }
                }
                else {
                    Toaster.makeToast(ViewTrialLogActivity.this, "Please subscribe to the experiment to edit trials");
                }
                }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        changeVisibility();
    }


    private void changeVisibility(){
        if (currentExperiment.getNumTrials() == 0){
            dropdown.setVisibility(View.INVISIBLE);
        }else{
            dropdown.setVisibility(View.VISIBLE);
        }
    }

    private void setupData(){


        // get all experiment data from firestore
        currentExperiment.getTrialController().getTrialLogData(this, filters);


    }
    private void setUpList(){
        listView = findViewById(R.id.trialListView);
        adapter = new TrialAdapter(getApplicationContext(), 0, trialLog.getTrials(),trialType,expID);
        listView.setAdapter(adapter);
    }

    @Override
    public void onOkPressed(BinomialTrial btrial){
        this.trialLog.set(entry_pos, btrial);
        setUpList();
    }


    @Override
    public void onOkPressed(CountTrial ctrial) {
        this.trialLog.set(entry_pos, ctrial);
        setUpList();

    }

    @Override
    public void onOkPressed(MeasurementTrial mtrial) {
        this.trialLog.set(entry_pos, mtrial);
        setUpList();
    }


    @Override
    public void onDoneGetTrials(TrialLog trialLog) {
        this.trialLog = trialLog;
        trialArrayList = trialLog.getTrials();
        adapter.clear();
        adapter.addAll(trialArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDoneGetIsSubscribed(Boolean result) {
        this.subscribed = result;
        if(result) {
            subButton.setText(R.string.unsubscribe);
        }
        else {
            subButton.setText(R.string.subscribe);
        }
    }

    @Override
    public void onDoneGetUser(User user) {
        this.currentUser = user;
        this.currentExperiment.getSubController().isSubscribed(user, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
    }

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        this.currentExperiment = experiment;
        this.isOwner = this.currentExperiment.getOwnerID().equals(FirebaseAuth.getInstance().getUid());
        if(currentExperiment.getStillRunning()){

            endButton.setText("End");
        }
        else{
            endButton.setText("Start");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentExperiment.getTrialController().getTrialLogData(this, filters);
        currentExperiment.getTrialController().getExperimenterIds(this);

    }

    @Override
    public void onDoneGetTrial(Trial trial) {
        reviewedTrial = trial.getData();
    }

    @Override
    public void onDoneGetExperimenterIds(ArrayList<String> ids){
        ids.add(0,"Filter Experimenter...");
        dropAdapter = new DropdownAdapter(this, R.layout.general_content,ids);
        dropdown.setAdapter(dropAdapter);
    }
}