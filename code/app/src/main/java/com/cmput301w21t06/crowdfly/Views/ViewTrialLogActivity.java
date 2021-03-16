
//this class displays trials within an experiment and has other functionalities

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.TrialAdapter;
import com.cmput301w21t06.crowdfly.Models.NewTrial;
import com.cmput301w21t06.crowdfly.Models.Statistics;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ViewTrialLogActivity extends AppCompatActivity {
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button addButton;
    static Integer counter = 0;
    public TrialAdapter adapter;
    private Button questionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);

        //only update the trialtype once per experiment
        if (counter < 1) {
            trialType =  getIntent().getStringExtra("trialType");
        }
        expID = Integer.parseInt(getIntent().getStringExtra("expID"));
        trialLog = TrialLog.getTrialLog();

        //setup the data
        setupData();
        setUpList();

        questionButton = findViewById(R.id.questionButton);
        questionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTrialLogActivity.this, ViewQuestionLogActivity.class);
                startActivity(intent);
            }
        });

        addButton = findViewById(R.id.addButton);


        //add trials
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                Intent intent = new Intent(getApplicationContext(), NewTrial.class);
                startActivity(intent);
            }
        });

        //delete trials
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                trialArrayList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }
    private void setupData(){
        String itemDescription = getIntent().getStringExtra("trialDesc");
        String entrySuccesses = getIntent().getStringExtra("success");
        String entryFailure = getIntent().getStringExtra("failure");

        if (counter >= 1) {
            Trial trial = new Trial(itemDescription, entrySuccesses, entryFailure);
            trialArrayList.add(trial);
        }

    }
    private void setUpList(){
        listView = findViewById(R.id.trialListView);
        adapter = new TrialAdapter(getApplicationContext(), 0, trialArrayList);
        listView.setAdapter(adapter);
    }


}