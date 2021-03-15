package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentContent;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ViewExperimentLogActivity extends AppCompatActivity implements CrowdFlyFirestore.OnDoneGetExpListener {
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
        new CrowdFlyFirestore().getExperimentData(this);

        experimentListView = findViewById(R.id.experiment_list);
        btnAddExperiment = findViewById(R.id.experiment_add);
        btnMap = findViewById(R.id.experiment_map);
        btnSearch = findViewById(R.id.experiment_search);

        expAdapter = new ExperimentContent(this, experimentLog.getExperiments());
        experimentListView.setAdapter(expAdapter);

        btnAddExperiment.setOnClickListener(new View.OnClickListener() {

            // should lead to a new activity, but just manually adding experiments for now
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewExperimentLogActivity.this, AddExperimentActivity.class));
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
