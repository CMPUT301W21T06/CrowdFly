
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
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.NewTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ViewTrialLogActivity extends AppCompatActivity implements EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener, CrowdFlyFirestore.OnDoneGetTrialsListener {
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private ListView listView;
    private Button addButton;
    private Button questionButton;
    static Integer counter = 0;
    static int entry_pos;
    public TrialAdapter adapter;
    static public String trialType;
    static public int expID;
    private TrialLog trialLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trial_log);

        //only update the trialtype once per experiment

        if(counter < 1){
            trialType =  getIntent().getStringExtra("trialType");
            expID = Integer.parseInt(getIntent().getStringExtra("expID"));
        }

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
        questionButton = findViewById(R.id.questionButton);
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
                counter += 1;
                Intent intent = new Intent(getApplicationContext(), NewTrial.class);
                intent.putExtra("trialType", trialType);
                intent.putExtra("expID", String.valueOf(expID));
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
//        String itemDescription = getIntent().getStringExtra("trialDesc");
//        String entrySuccesses = getIntent().getStringExtra("success");
//        String entryFailure = getIntent().getStringExtra("failure");
//        String entryCount = getIntent().getStringExtra("count");
//        String entryMeasurement = getIntent().getStringExtra("measurement");



        // get all experiment data from firestore
        new CrowdFlyFirestore().getTrialData(expID, this);


//        if (counter >= 1) {
//            // In the viewTrialLogActivity, there is a placeholder called trialType, when being passed
//            // an experiment, this experiment will have an attribute that specifies what trial type it
//            // allows in its array adapter, the trialType identifier can take 3 different values that
//            // represent the 3 different trial types
//
//            Log.e("running?", "yes");
//            if(trialType.equals("binomial")){
//                Log.d("entryFailures",entryFailure);
//                Log.d("entrySuccesses", entrySuccesses);
//                Trial trial = new BinomialTrial(itemDescription, entrySuccesses, entryFailure);
//                trialArrayList.add(trial);
//            }else if (trialType.equals("count")){
//                Log.d("entryCount",entryCount);
//                Trial trial = new CountTrial(itemDescription, entryCount);
//                trialArrayList.add(trial);
//            }else if (trialType.equals("measurement")){
//                Log.d("entryMeasurement",entryMeasurement);
//                Trial trial = new MeasurementTrial(itemDescription, entryMeasurement);
//                trialArrayList.add(trial);
//            }
//        }

    }
    private void setUpList(){
        listView = findViewById(R.id.trialListView);
        //adapter = new TrialAdapter(getApplicationContext(), 0, trialArrayList);
        adapter = new TrialAdapter(getApplicationContext(), 0, trialLog.getTrials());
        Log.e("triallog", String.valueOf(trialLog.getTrials()));
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


    @Override
    public void onDoneGetTrials(TrialLog trialLog) {
        this.trialLog = trialLog;
        adapter.notifyDataSetChanged();
    }
}