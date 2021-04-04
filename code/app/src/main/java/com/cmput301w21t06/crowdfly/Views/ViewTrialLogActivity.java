
//this class displays trials within an experiment and has other functionalities

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.cmput301w21t06.crowdfly.Models.TrialFactory;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        Toaster,
        NavigationView.OnNavigationItemSelectedListener {

    private final String SELECTION = "COM.CMPUT301W21T06.CROWDFLY.MAP.ALL";
    private final String EXP = "COM.CMPUT301W21T06.CROWDFLY.MAP.EXP";

    public static final String EXPERIMENT_IS_NO_LONGER_ACTIVE = "This experiment is no longer active.";
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button mapButton;
    private Button addButton;
    private Button questionButton;
    private Button qrButton;
    private Button subButton;
    private Button endButton;
    private Button statButton;
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
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);
        filters = new ArrayList<String>();
        mapButton = findViewById(R.id.mapButton);
        endButton = findViewById(R.id.endButton);
        dropdown = findViewById(R.id.dropDown);
        statButton = findViewById(R.id.statisticButton);
        //only update the trialtype once per experiment
        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID, this);
        trialType = currentExperiment.getType();
        trialLog = TrialLog.getTrialLog();
        setUpList();
        UserController.getUserProfile(UserController.reverseConvert(FirebaseAuth.getInstance().getUid()), this);
        //setup the data
        setupData();

        drawerLayout = findViewById(R.id.drawer_trials);
        navigationView = findViewById(R.id.nav_view_trials);
        toolbar = findViewById(R.id.toolbar_trails);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.userFBID);
        navUserId.setText(currentUser.getUserID());

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
        questionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTrialLogActivity.this, ViewQuestionLogActivity.class);
                startActivity(intent);
            }
        });
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTrialLogActivity.this, ViewStatisticActivity.class);
                intent.putExtra("trialType", trialType);
                intent.putExtra("expID", String.valueOf(expID));
                startActivity(intent);

            }
        });
        qrButton = findViewById(R.id.QRButton);
        qrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isCodeScanningAvailable()) {
                    Toaster.makeToast(ViewTrialLogActivity.this, "This feature is not available for experiments with this trial type");
                }
                else if(!currentExperiment.getStillRunning()){
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
                }
                else if(subscribed || isOwner){
                    Intent intent = new Intent(ViewTrialLogActivity.this, ViewQRActivity.class);
                    intent.putExtra("expID", expID);
                    startActivity(intent);
                }
                else {
                    Toaster.makeToast(ViewTrialLogActivity.this,"Please subscribe to the experiment to use this feature");
                }
            }

            private boolean isCodeScanningAvailable() {
                return !(trialType.equals(getString(R.string.binomial)) || trialType.equals(getString(R.string.count)));
            }
        });
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
                                Toaster.makeCrispyToast(ViewTrialLogActivity.this,"The minimum number of trials have not yet been achieved!");
                            }
                        }
                        ExperimentController.setExperimentData(currentExperiment);
                    }
                    else {
                        Log.e("rr","run");
                        Toaster.makeCrispyToast(ViewTrialLogActivity.this, "Only the owner can end an experiment!");
                    }
                }
            }
        });
        questionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTrialLogActivity.this, ViewQuestionLogActivity.class);
                startActivity(intent);
            }
        });


        //add trials
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
                    return;
                }
                if(subscribed || isOwner){

                    Intent intent = new Intent(getApplicationContext(), NewTrialActivity.class);
                    intent.putExtra("trialType", trialType);
                    intent.putExtra("expID", String.valueOf(expID));
                    startActivityForResult(intent,0);
                }
                else {
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, "Please subscribe to the experiment to add trials");
                }

            }
        });



        //delete trials
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
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
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this,"Please subscribe to the experiment to remove trials");
                }
                return false;
            }
        });

        //edit trials
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!currentExperiment.getStillRunning()){
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, EXPERIMENT_IS_NO_LONGER_ACTIVE);
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
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, "Please subscribe to the experiment to edit trials");
                }
                }


        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String[]> locations = new HashMap<String, String[]>();
                if (!currentExperiment.getRegion().matches("")){
                    String[] arr = {currentExperiment.getRegion(),currentExperiment.getOwnerID()};
                    locations.put(currentExperiment.getExperimentId(),arr);
                }
                for (Trial trial : currentExperiment.getTrials()){
                    if (!trial.getLocation().matches("")){
                        String[] arr = {trial.getLocation(),trial.getExperimenterID(),"T"};
                        locations.put(trial.getTrialID(),arr);
                    }
                }
                if (locations.size() == 0){
                    Toaster.makeCrispyToast(ViewTrialLogActivity.this, "Locations were not required and no locations were voluntarily entered!");
                }
                else {
                    if (!currentExperiment.getRegionEnabled()){
                        Toaster.makeCrispyToast(ViewTrialLogActivity.this,"Location were not required, but displaying those voluntarily entered!");
                    }
                    Intent intent = new Intent(ViewTrialLogActivity.this, ViewLocationActivity.class);
                    intent.putExtra(SELECTION, true);
                    intent.putExtra(EXP, locations);
                    startActivity(intent);
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
        if(btrial!=null){
            this.trialLog.set(entry_pos, btrial);
            setUpList();
        }
    }


    @Override
    public void onOkPressed(CountTrial ctrial) {
        if(ctrial!=null){
            this.trialLog.set(entry_pos, ctrial);
            setUpList();
        }
    }

    @Override
    public void onOkPressed(MeasurementTrial mtrial) {
        if(mtrial!=null) {
            this.trialLog.set(entry_pos, mtrial);
            setUpList();
        }
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
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Object> result = (HashMap<String, Object>) data.getSerializableExtra("trialData");
                try {
                    Trial trialAdd = new TrialFactory().getTrialInferType(result);
                    currentExperiment.getTrialController().addTrialData(trialAdd, expID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
               // Do nothing?
                Log.d("NEW TRIAL RESULT", "Cancelled adding a new trial");
            }
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.hamHome:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.hamAccount:
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra(TAG, UserController.reverseConvert(currentUser.getUserID()));
                startActivity(intent);
                break;
            case R.id.hamExperiment:
                break;

        }
        return true;
    }
}