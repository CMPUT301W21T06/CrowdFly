package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.cmput301w21t06.crowdfly.Controllers.DropdownAdapter;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentAdapter;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Database.SearchController;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Shows all the experiments in a list view
 * Map and search buttons not implemented
 */
public class ViewExperimentLogActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpLogListener, Toaster, NavigationView.OnNavigationItemSelectedListener, CompletionHandler {
    private ListView experimentListView;
    private ExperimentAdapter expAdapter;
    private ExperimentLog experimentLog;
    private ArrayList<Experiment> experimentsList;
    private final String userID = FirebaseAuth.getInstance().getUid();
    private DropdownAdapter symbolAdapter;
    private DropdownAdapter activeAdapter;
    private DropdownAdapter regionAdapter;
    private final ArrayList<String> symbolList = new ArrayList<String>(Arrays.asList("N/A", "<", ">", ">=", "<=", "=", "!=", "TO"));
    private final ArrayList<String> activeList = new ArrayList<String>(Arrays.asList("N/A", "Active", "Not Active"));
    private final ArrayList<String> regionList = new ArrayList<String>(Arrays.asList("N/A", "Region Enforced", "Not Enforced"));
    Button btnAddExperiment;
    Button btnSearch;
    EditText searchExp;
    TextView filterText;
    ViewSwitcher viewSwitcher;
    Spinner symbols;
    EditText numTrials;
    EditText numTrialsLeft;
    Button doneButton;
    Spinner activeSpinner;
    Spinner regionSpinner;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment_log);
        SearchController.clearMasks();
        experimentLog = ExperimentLog.getExperimentLog();
        experimentsList = experimentLog.getExperiments();
        viewSwitcher = findViewById(R.id.viewSwitcher);
        btnSearch = findViewById(R.id.experimentSearchButton);
        searchExp = findViewById(R.id.searchExperiment);
        experimentListView = findViewById(R.id.experiment_list);
        doneButton = findViewById(R.id.buttonDoneLog);
        btnAddExperiment = findViewById(R.id.experimentAdd);
        regionSpinner = findViewById(R.id.regionSpinner);
        activeSpinner = findViewById(R.id.activeSpinner);
        numTrials = findViewById(R.id.numTrialsRight);
        numTrialsLeft = findViewById(R.id.numTrialsLeft);
        filterText = findViewById(R.id.trialsFilter);
        symbols = findViewById(R.id.signSpinner);
        setVisibility();
        expAdapter = new ExperimentAdapter(this, experimentsList);
        experimentListView.setAdapter(expAdapter);
        // get all experiment data from firestore
        ExperimentController.getExperimentLogData(this);

        drawerLayout = findViewById(R.id.drawer_experiment);
        navigationView = findViewById(R.id.nav_view_experiment);
        toolbar = findViewById(R.id.toolbar_experiment);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.userFBID);
        navUserId.setText(userID);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
                Toaster.makeToast(ViewExperimentLogActivity.this, "Aftering entering text into the main box, click done to perform the search or long click to cancel!");
                setVisibility();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String symbolString = symbols.getSelectedItem().toString();
                String trialFilter = numTrials.getText().toString();
                String trialLeftFilter = numTrialsLeft.getText().toString();
                String regionFilter = regionSpinner.getSelectedItem().toString();
                String activeFilter = activeSpinner.getSelectedItem().toString();
                String searchFilter = searchExp.getText().toString();


                if (!symbolString.matches("TO") && !SearchController.validTrialFilter(symbolString,trialFilter)){
                    Toaster.makeToast(ViewExperimentLogActivity.this,"Filtering for trials requires a number and a symbol, without those, related input will be disregarded!");
                }
                else if (symbolString.matches("TO") && !SearchController.validTrialToFilter(trialLeftFilter,trialFilter)){
                    Toaster.makeToast(ViewExperimentLogActivity.this,"Filtering for trials requires two numbers and a symbol, without those, related input will be disregarded!");
                }
                SearchController.query(ViewExperimentLogActivity.this, symbolString,trialFilter,trialLeftFilter, regionFilter,activeFilter,searchFilter);
                clearBoxes();
            }
        });

        searchExp.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                cancelSearch();
                ExperimentController.getExperimentLogData(ViewExperimentLogActivity.this);
                return false;
            }
        });

        symbols.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == symbolList.indexOf("TO")){
                    numTrialsLeft.setVisibility(View.VISIBLE);
                }
                else{
                    numTrialsLeft.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddExperiment.setOnClickListener(new View.OnClickListener() {

            // should lead to a new activity, but just manually adding experiments for now
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ViewExperimentLogActivity.this, AddExperimentActivity.class), 0);
                Log.e("TT","RETURNED");
                expAdapter.notifyDataSetChanged();
            }
        });

        experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cancelSearch();
                Experiment experiment = (Experiment) adapterView.getAdapter().getItem(i);
                String expID = experiment.getExperimentId();
                Intent intent = new Intent(getApplicationContext(), ViewTrialLogActivity.class);
                intent.putExtra("expID", String.valueOf(expID));
                startActivity(intent);

            }
        });
        //delete experiment on long click
        experimentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                cancelSearch();
                Log.e("R","LONG CLICK");
                Experiment exp = experimentsList.get(i);
                if (userID.matches(exp.getOwnerID())) {
                    ExperimentController.deleteExperiment(exp.getExperimentId(), exp);
                    ExperimentController.getExperimentLogData(ViewExperimentLogActivity.this);
                }
                else{
                    Toaster.makeCrispyToast(ViewExperimentLogActivity.this, "Only the owner may unpublish an experiment!");
                }
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        cancelSearch();
        ExperimentController.getExperimentLogData(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExperimentController.getExperimentLogData(this);
    }

    /**
     * Handles projecting data to list view once data retrieved from database
     */
    @Override
    public void onDoneGetExperiments() {
        experimentsList = experimentLog.getExperiments();
        Log.e("GOTTEN",String.valueOf(experimentsList));
        expAdapter.clear();
        expAdapter.addAll(experimentsList);
        expAdapter.notifyDataSetChanged();
    }

    private void setVisibility(){
        int target = View.VISIBLE;
        if (symbols.getVisibility() == target){
            target = View.INVISIBLE;
            clearBoxes();
        }
        else {
            symbolAdapter = new DropdownAdapter(this, R.layout.general_layout_min, symbolList, false);
            symbols.setAdapter(symbolAdapter);
            activeAdapter = new DropdownAdapter(this, R.layout.general_layout_min, activeList, false);
            activeSpinner.setAdapter(activeAdapter);
            regionAdapter = new DropdownAdapter(this, R.layout.general_layout_min, regionList, false);
            regionSpinner.setAdapter(regionAdapter);

        }
        numTrialsLeft.setVisibility(View.INVISIBLE);
        filterText.setVisibility(target);
        symbols.setVisibility(target);
        numTrials.setVisibility(target);
        activeSpinner.setVisibility(target);
        regionSpinner.setVisibility(target);
        doneButton.setVisibility(target);

    }

    private void clearBoxes(){
        numTrials.setText("");
        searchExp.setText("");
        numTrialsLeft.setText("");
    }
    private void cancelSearch(){
        if (doneButton.getVisibility() == View.VISIBLE){
            setVisibility();
            viewSwitcher.showPrevious();
        }
        SearchController.clearMasks();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.hamHome:
                drawerLayout.closeDrawers();
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.hamAccount:
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra(TAG, UserController.reverseConvert(userID));
                startActivity(intent);
                break;
            case R.id.hamExperiment:
                break;

        }
        return true;
    }
    @Override
    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
        if (e == null){
            Log.e("Algolia","Operation completed " + String.valueOf(jsonObject));
            try {
                JSONArray hits = jsonObject.getJSONArray("hits");
                Log.e("ddd",String.valueOf(hits.length()));
                if (hits.length() != 0) {
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hit = hits.getJSONObject(i);
                        SearchController.addMask((String) hit.get("experimentID"));
                    }
                    ExperimentController.getExperimentLogData(this);
                }
                else{
                    SearchController.addMask("N/A");
                    ExperimentController.getExperimentLogData(this);

                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                SearchController.addMask("N/A");
                ExperimentController.getExperimentLogData(this);
            }

        }
        else{
            Log.e("Algolia",String.valueOf(e));
            SearchController.addMask("N/A");
            ExperimentController.getExperimentLogData(this);
        }
    }
}
