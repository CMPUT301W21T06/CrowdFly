package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Fragment to allow controlled edits to a created binomial trial
 */
public class EditBinomialTrialFragment extends DialogFragment {
    private EditText successes, failures, description;
    private OnFragmentInteractionListener listener;
    String userID = FirebaseAuth.getInstance().getUid();
    public interface OnFragmentInteractionListener {
        void onOkPressed(BinomialTrial trial);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragListner");
        }
    }

    public static EditBinomialTrialFragment newInstance(BinomialTrial new_trial){
        Bundle args = new Bundle();
        args.putInt("suc", new_trial.getSuccesses());
        args.putInt("fail", new_trial.getFailures());
        args.putString("desc", new_trial.getDescription());

        EditBinomialTrialFragment fragment = new EditBinomialTrialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_edit_binomial_trial_fragment, null);

        description = view.findViewById(R.id.binDescription);
        successes = view.findViewById(R.id.binSuccesses);
        failures = view.findViewById(R.id.binFailures);

        if (getArguments() != null){
            description.setText(getArguments().getString("desc"));
            successes.setText(String.valueOf(getArguments().getInt("suc")));
            failures.setText(String.valueOf(getArguments().getInt("fail")));
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
                        String successes1 = String.valueOf(successes.getText());
                        String failures1 = String.valueOf(failures.getText());
                        int successes2 = 0;
                        int failures2 = 0;
                        if (successes1.length() != 0){
                            successes2 = Integer.parseInt(successes1);
                        }
                        if (failures1.length() != 0){
                            failures2 = Integer.parseInt(failures1);
                        }
                        String description1 = description.getText().toString();

                        listener.onOkPressed(new BinomialTrial(description1, successes2,failures2, "",userID));
                    }
                }).create();
    }


}