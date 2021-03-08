
//this class displays trials within an experiment and has other functionalities

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;


import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.TrialAdapter;
import com.cmput301w21t06.crowdfly.Models.NewTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ViewTrialLogActivity extends AppCompatActivity {
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button addButton;
    static Integer counter = 0;
    public TrialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);

        //setup the data
        setupData();
        setUpList();

        addButton = findViewById(R.id.addButton);

        //adding trials
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                Intent intent = new Intent(getApplicationContext(), NewTrial.class);
                startActivity(intent);
            }
        });

        //long click delete function
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