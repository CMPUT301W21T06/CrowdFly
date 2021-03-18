package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentContent;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

public class ViewExperimentLogActivity extends AppCompatActivity implements CrowdFlyFirestore.OnDoneGetExpLogListener {
    private ListView experimentListView;
    private ArrayAdapter<Experiment> expAdapter;
    private ExperimentContent experimentContent;
    private ExperimentLog experimentLog;

    Button btnAddExperiment;
    Button btnMap;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_experiment_log);
        experimentLog = ExperimentLog.getExperimentLog();

        // get all experiment data from firestore
        new CrowdFlyFirestore().getExperimentLogData(this);

        experimentListView = findViewById(R.id.experiment_list);
        btnAddExperiment = findViewById(R.id.experiment_add);
        btnMap = findViewById(R.id.experiment_map);
        btnSearch = findViewById(R.id.experiment_search);

        expAdapter = new ExperimentContent(this, experimentLog.getExperiments());
        //Log.e("experimentLog", String.valueOf(experimentLog.getExperiments()));
        experimentListView.setAdapter(expAdapter);

        btnAddExperiment.setOnClickListener(new View.OnClickListener() {

            // should lead to a new activity, but just manually adding experiments for now
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewExperimentLogActivity.this, AddExperimentActivity.class));
            }
        });

        experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String trialType = getIntent().getStringExtra("trialType");
                Experiment experiment = (Experiment) adapterView.getAdapter().getItem(i);
                String trialType = experiment.getDescription();
                String expID = experiment.getExperimentId();
                Intent intent = new Intent(getApplicationContext(), ViewTrialLogActivity.class);
                intent.putExtra("trialType", trialType);
                intent.putExtra("expID", String.valueOf(expID));
                startActivity(intent);

            }
        });
        //delete experiment on long click
        experimentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CrowdFlyFirestore().deleteExperiment(expAdapter.getItem(i).getExperimentId());
                experimentLog.removeExperiment(i);
                expAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    @Override
    public void onDoneGetExperiments(ExperimentLog expLog) {
        this.experimentLog = expLog;
        expAdapter.notifyDataSetChanged();
//        Log.d("abc", experimentLog.getExperiment(expLog));
    }
}
