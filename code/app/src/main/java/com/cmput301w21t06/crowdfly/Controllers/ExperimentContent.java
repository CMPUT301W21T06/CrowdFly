package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class ExperimentContent extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;

    public ExperimentContent(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        // get all content from the page
        TextView description = view.findViewById(R.id.exp_description);
        TextView ownerName = view.findViewById(R.id.exp_ownerName);
        TextView status = view.findViewById(R.id.exp_status);
        TextView numTrials = view.findViewById(R.id.exp_numTrials);
        TextView region = view.findViewById(R.id.exp_region);
        Button btnSubscribe = view.findViewById(R.id.button_subscribe);

        // set content description
        description.setText(experiment.getDescription());
        ownerName.setText(experiment.getOwnerID());
        status.setText(experiment.getStatus() ? "Active" : "Not Active" );
        numTrials.setText(String.valueOf(experiment.getMinTrials()));
        region.setText(experiment.getRegion());

        return view;
    }
}
