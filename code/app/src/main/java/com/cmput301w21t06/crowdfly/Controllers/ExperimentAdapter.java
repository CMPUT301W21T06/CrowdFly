package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

/**
 * this is the experiment adapter that adapts a view in a specific position
 */
public class ExperimentAdapter extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;

    /**
     * this is the constructor of the experiment adapter
     * @param context
     * @param experiments
     */
    public ExperimentAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    /**
     * this is responsible for creating the views for a particular position
     * @param position
     * @param convertView
     * @param parent
     * @return
     *      a view at a particular position
     */
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

        // set content description
        description.setText(experiment.getDescription());
        ownerName.setText(UserController.reverseConvert(experiment.getOwnerID()));
        status.setText(experiment.getStillRunning() ? "Active" : "Not Active" );
        numTrials.setText(String.valueOf(experiment.getMinTrials()));
        region.setText(experiment.getRegion());

        return view;
    }
}
