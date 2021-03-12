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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.R;

public class EditMeasureTrialFragment extends DialogFragment {

    private EditText measurement;
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

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_edit_measure_trial_fragment, null);

        measurement = view.findViewById(R.id.measurementInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Entry")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int measurement1 = Integer.parseInt(measurement.getText().toString());
                        listener.onOkPressed(new MeasurementTrial("", measurement1));
                        //Log.e("brebs", itemDate1);
                    }
                }).create();
    }
}