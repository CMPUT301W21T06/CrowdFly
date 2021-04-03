package com.cmput301w21t06.crowdfly.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTrialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTrialFragment extends DialogFragment {

    private static final String TRIAL_TYPE = "trialType";
    private static final String USER_ID = "userID";

    private String trialType;
    private String userID;
    private int layout;
    private OnNewTrialListener listener;

    public NewTrialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trialType String representation of the trial type
     * @return A new instance of fragment NewTrialFragment.
     */

    public static NewTrialFragment newInstance(String trialType, String userID) {
        NewTrialFragment fragment = new NewTrialFragment();
        Bundle args = new Bundle();
        args.putString(TRIAL_TYPE, trialType);
        args.putString(USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewTrialListener) {
            listener = (OnNewTrialListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            trialType = getArguments().getString(TRIAL_TYPE);
            userID = getArguments().getString(USER_ID);
            if (trialType.equals(getString(R.string.binomial))) {
                layout = R.layout.activity_edit_binomial_trial_fragment;
            } else if (trialType.equals(getString(R.string.count))) {
                layout = R.layout.activity_edit_count_trial_fragment;
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder = builder
                .setView(view)
                .setTitle("Set Trial Result")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressed(null);
                    }
                });
        if (layout == R.layout.activity_edit_count_trial_fragment) {
            // EditText successes, failures, description;
            EditText countTextView, descriptionTextView;
            countTextView = view.findViewById(R.id.countInput);
            descriptionTextView = view.findViewById(R.id.countDescInput);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String count = String.valueOf(countTextView.getText());
                    String description = descriptionTextView.getText().toString();
                    int countInt = 0;
                    if (count.length() != 0) {
                        countInt = Integer.parseInt(count);
                    }
                    listener.onOkPressed(new CountTrial(description, countInt, "", "", userID));
                }
            });
        } else if (layout == R.layout.activity_edit_binomial_trial_fragment) {
            EditText successesTextView, failuresTextView, descriptionTextView;
            descriptionTextView = view.findViewById(R.id.binDescription);
            successesTextView = view.findViewById(R.id.binSuccesses);
            failuresTextView = view.findViewById(R.id.binFailures);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String successes = String.valueOf(successesTextView.getText());
                    String failures = String.valueOf(failuresTextView.getText());
                    int successesInt = 0;
                    int failuresInt = 0;
                    if (successes.length() != 0) {
                        successesInt = Integer.parseInt(successes);
                    }
                    if (failures.length() != 0) {
                        failuresInt = Integer.parseInt(failures);
                    }
                    String description = descriptionTextView.getText().toString();
                    listener.onOkPressed(new BinomialTrial(description, successesInt, failuresInt, "", "", userID));
                }
            });
        }

        return builder.create();


    }

    public interface OnNewTrialListener {
        void onOkPressed(Trial trial);
    }
}