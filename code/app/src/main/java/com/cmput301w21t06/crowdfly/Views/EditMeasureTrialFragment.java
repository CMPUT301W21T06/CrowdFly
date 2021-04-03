package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Fragment to allow controlled edits to a created measurment trial
 */
public class EditMeasureTrialFragment extends DialogFragment {
    String userID = FirebaseAuth.getInstance().getUid();
    private EditText measurement, description;
    private EditMeasureTrialFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(MeasurementTrial trial);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof EditMeasureTrialFragment.OnFragmentInteractionListener){
            listener = (EditMeasureTrialFragment.OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragListner");
        }
    }
    public static EditMeasureTrialFragment newInstance(MeasurementTrial new_trial){
        Bundle args = new Bundle();
        args.putDouble("measure", new_trial.getMeasurement());
        args.putString("desc", new_trial.getDescription());

        EditMeasureTrialFragment fragment = new EditMeasureTrialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_edit_measure_trial_fragment, null);

        measurement = view.findViewById(R.id.measurementInput);
        description = view.findViewById(R.id.measurementDesc);

        if (getArguments() != null){
            description.setText(getArguments().getString("desc"));
            measurement.setText(String.valueOf(getArguments().getDouble("measure")));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Entry")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressed(null);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String measurement1 = String.valueOf(measurement.getText());
                        String description1 = description.getText().toString();
                        Double measurement2 = 0.;
                        if (measurement1.length() != 0){
                            measurement2 = Double.parseDouble(measurement1);
                        }
                        listener.onOkPressed(new MeasurementTrial(description1, measurement2, "", userID));
                    }
                }).create();
    }
}