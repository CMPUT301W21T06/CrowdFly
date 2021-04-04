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

import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Fragment to allow controlled edits to a created count trial
 */
public class EditCountTrialFragment extends DialogFragment {
    String userID = FirebaseAuth.getInstance().getUid();
    private String loc;

    private EditText count, description;
    private EditCountTrialFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(CountTrial trial);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof EditCountTrialFragment.OnFragmentInteractionListener){
            listener = (EditCountTrialFragment.OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragListner");
        }
    }

    public static EditCountTrialFragment newInstance(CountTrial new_trial){
        Bundle args = new Bundle();
        args.putInt("count", new_trial.getCount());
        args.putString("desc", new_trial.getDescription());
        args.putString("loc", new_trial.getLocation());

        EditCountTrialFragment fragment = new EditCountTrialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_edit_count_trial_fragment, null);

        count = view.findViewById(R.id.countInput);
        description = view.findViewById(R.id.countDescInput);
        loc = "";

        if (getArguments() != null){
            description.setText(getArguments().getString("desc"));
            count.setText(String.valueOf(getArguments().getInt("count")));
            loc = getArguments().getString("loc");

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
                        String count1 = String.valueOf(count.getText());
                        String description1 = description.getText().toString();
                        int count2 = 0;
                        if (count1.length() != 0){
                            count2 = Integer.parseInt(count1);
                        }
                        listener.onOkPressed(new CountTrial(description1, count2, "", userID, loc));
                    }
                }).create();
    }
}