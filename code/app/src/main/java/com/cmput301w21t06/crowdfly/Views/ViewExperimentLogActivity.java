package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentAdapter;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Shows all the experiments in a list view
 * Map and search buttons not implemented
 */
public class ViewExperimentLogActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpLogListener, Toaster, NavigationView.OnNavigationItemSelectedListener {
    private ListView experimentListView;
    private ExperimentAdapter expAdapter;
    private ExperimentLog experimentLog;
    private ArrayList<Experiment> experimentsList;
    private final String userID = FirebaseAuth.getInstance().getUid();
    Button btnAddExperiment;
    Button btnSearch;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment_log);
        experimentLog = ExperimentLog.getExperimentLog();
        experimentsList = experimentLog.getExperiments();
        experimentListView = findViewById(R.id.experiment_list);
        btnAddExperiment = findViewById(R.id.experiment_add);
        btnSearch = findViewById(R.id.experiment_search);
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

    //added
}
