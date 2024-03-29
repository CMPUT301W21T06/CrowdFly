package com.cmput301w21t06.crowdfly.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private boolean constantIsPublished;
    private boolean constantStillRunning;
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
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        // get all content from the page
        TextView description = view.findViewById(R.id.exp_description);
        TextView ownerName = view.findViewById(R.id.exp_ownerName);
        TextView published = view.findViewById(R.id.exp_published);
        TextView numTrials = view.findViewById(R.id.exp_numTrials);
        TextView region = view.findViewById(R.id.exp_region);
        TextView status = view.findViewById(R.id.exp_status);

        // set content description
        description.setText(experiment.getDescription());
        ownerName.setText(UserController.reverseConvert(experiment.getOwnerID()));
        published.setText(experiment.getIsPublished() ? Html.fromHtml("<b><font color='blue'>" +"Published"+"</b></font>" ) : "Unpublished");
        constantIsPublished = experiment.getIsPublished();
        Log.e("is published", String.valueOf(constantIsPublished));
        status.setText(experiment.getStillRunning() ? "Active" : Html.fromHtml("<b><font color='red'>" +"Inactive"+"</b></font>" ));
        constantStillRunning = experiment.getStillRunning();
        Log.e("still Running", String.valueOf(constantStillRunning));

        numTrials.setText(String.valueOf(experiment.getMinTrials()));
        RegionViewSetter.setRegion(region,experiment.getRegion());
        return view;
    }
}
