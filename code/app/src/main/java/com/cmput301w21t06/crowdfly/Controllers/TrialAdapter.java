package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.cmput301w21t06.crowdfly.Views.ViewStatisticActivity;

import java.util.List;

public class TrialAdapter extends ArrayAdapter<Trial> {

    private String experimentID;
    private String trialType;
    private Context context;

    public TrialAdapter(@NonNull Context context, int resource, @NonNull List<Trial> trialList, String trialType, String expID) {
        super(context, resource, trialList);
        this.context = context;
        this.trialType = trialType;
        this.experimentID = expID;
    }
    //learned about getView ArrayAdapter method from vipul mittal: https://stackoverflow.com/users/1423227/vipul-mittal
    //from stackoverflow
    //from: https://stackoverflow.com/questions/6442054/how-does-arrayadapter-getview-method-works
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Trial trial = getItem(position);
        Log.e("TA-trialID", String.valueOf(trial.getTrialID()));

        //learned from Bertram Gilfoyle: https://stackoverflow.com/users/7594961/bertram-gilfoyle
        //from stackoverflow
        //from: https://stackoverflow.com/questions/51729036/what-is-layoutinflater-and-how-do-i-use-it-properly
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trial_content, parent, false);
        }

        //set variables to particular views

        TextView trialInfo = (TextView) convertView.findViewById(R.id.itemSuccesses);
        TextView desc = convertView.findViewById(R.id.itemDescription);
        Button statbtn = convertView.findViewById(R.id.statButton);

        statbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewStatisticActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        // checking the exact type of the trial so it can correctly set the text boxes in the content in the list view
        // specific trial types have specific methods
        if (trialType.equals("binomial")){
            BinomialTrial bTrial = (BinomialTrial)trial;
            String successDisplay = "S:";
            String failureDisplay = " F:";
            trialInfo.setText(successDisplay+bTrial.getSuccesses()+failureDisplay+bTrial.getFailures());
        } else if (trialType.equals("count")){
            String countDisplay = "Count: ";
            CountTrial cTrial = (CountTrial)trial;
            trialInfo.setText(countDisplay+cTrial.getCount());
        } else if (trialType.equals("measurement")){
            String measurementDisplay = "Measurement: ";
            MeasurementTrial mTrial = (MeasurementTrial)trial;
            trialInfo.setText(measurementDisplay+mTrial.getMeasurement());
        }

        desc.setText(trial.getDescription());

        return convertView;
    }
}
