
//this class displays trials within an experiment and has other functionalities

package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.TrialAdapter;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.NewTrial;
import com.cmput301w21t06.crowdfly.Models.Statistics;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ViewTrialLogActivity extends AppCompatActivity implements EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener{
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button addButton;
    static Integer counter = 0;
    static int entry_pos;
    public TrialAdapter adapter;
    public String trialType = "binomial";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);

        //setup the data
        setupData();
        setUpList();

        addButton = findViewById(R.id.addButton);

        //add trials
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                Intent intent = new Intent(getApplicationContext(), NewTrial.class);
                intent.putExtra("trialtype", trialType);
                startActivity(intent);
                //if (trialType == "binomial"){
                //    EditBinomialTrialFragment editBinomialTrialFragment = new EditBinomialTrialFragment();
                //   editBinomialTrialFragment.show(getSupportFragmentManager(), "EDIT TEXT");
                //}

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

        //edit trials
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (trialType.equals("binomial")){
                    EditBinomialTrialFragment editBinomialTrialFragment = new EditBinomialTrialFragment();
                    entry_pos = i;
                    BinomialTrial btrial = (BinomialTrial) adapterView.getAdapter().getItem(i);
                    editBinomialTrialFragment.newInstance(btrial).show(getSupportFragmentManager(), "EDIT TEXT");
                }
                if (trialType.equals("count")){
                    EditCountTrialFragment editCountTrialFragment = new EditCountTrialFragment();
                    entry_pos = i;
                    CountTrial ctrial = (CountTrial) adapterView.getAdapter().getItem(i);
                    editCountTrialFragment.newInstance(ctrial).show(getSupportFragmentManager(), "EDIT TEXT");
                }
                if (trialType.equals("measurement")){
                    EditMeasureTrialFragment editMeasureTrialFragment = new EditMeasureTrialFragment();
                    entry_pos = i;
                    MeasurementTrial mtrial = (MeasurementTrial) adapterView.getAdapter().getItem(i);
                    editMeasureTrialFragment.newInstance(mtrial).show(getSupportFragmentManager(), "EDIT TEXT");}
                }
        });

    }
    private void setupData(){
        // grab import bundles from the intent
        String itemDescription = getIntent().getStringExtra("trialDesc");
        String entrySuccesses = getIntent().getStringExtra("success");
        String entryFailure = getIntent().getStringExtra("failure");
        String entryCount = getIntent().getStringExtra("count");
        String entryMeasurement = getIntent().getStringExtra("measurement");


        if (counter >= 1) {
            // In the viewTrialLogActivity, there is a placeholder called trialType, when being passed
            // an experiment, this experiment will have an attribute that specifies what trial type it
            // allows in its array adapter, the trialType identifier can take 3 different values that
            // represent the 3 different trial types


            if(trialType == "binomial"){
                Log.d("entryFailures",entryFailure);
                Log.d("entrySuccesses", entrySuccesses);
                Trial trial = new BinomialTrial(itemDescription, entrySuccesses, entryFailure);
                trialArrayList.add(trial);
            }else if (trialType == "count"){
                Log.d("entryCount",entryCount);
                Trial trial = new CountTrial(itemDescription, entryCount);
                trialArrayList.add(trial);
            }else if (trialType == "measurement"){
                Log.d("entryMeasurement",entryMeasurement);
                Trial trial = new MeasurementTrial(itemDescription, entryMeasurement);
                trialArrayList.add(trial);
            }
        }

    }
    private void setUpList(){
        listView = findViewById(R.id.trialListView);
        adapter = new TrialAdapter(getApplicationContext(), 0, trialArrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onOkPressed(BinomialTrial btrial){
        trialArrayList.set(entry_pos, btrial);
        setUpList();
    }


    @Override
    public void onOkPressed(CountTrial ctrial) {
        trialArrayList.set(entry_pos, ctrial);
        setUpList();

    }

    @Override
    public void onOkPressed(MeasurementTrial mtrial) {
        trialArrayList.set(entry_pos, mtrial);
        setUpList();
    }
}