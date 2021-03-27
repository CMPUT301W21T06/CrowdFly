package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TrialController {
    private CollectionReference trialsCollection;
    private ArrayList<Trial> trials = new ArrayList<Trial>();
    public TrialController(String eid) {
        trialsCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.trials(eid));
        setUp();
    }
    private void setUp(){
        trialsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                trials.clear();
                for (QueryDocumentSnapshot doc : response){
                    String type = doc.getString("type");
                    Trial trial = null;
                    Log.e("dbshit",doc.getString("trialID"));
                    switch (type) {
                        case "binomial":
                            trial = new BinomialTrial(doc.getData());
                            Log.e("shitdipper",trial.getTrialID());
                            break;
                        case "count":
                            trial = new CountTrial(doc.getData());
                            break;
                        case "measurement":
                            trial = new MeasurementTrial(doc.getData());
                            break;
                    }
                    if (trial != null) {
                        trials.add(trial);
                    }
                    else{
                        Log.e("TrialController", "Getting trials was a failure");
                    }
                }
            }
        });
    }

    public void getTrialLogData(CrowdFlyListeners.OnDoneGetTrialsListener onDoneGetTrialsListener){
        Collections.sort(trials);
        TrialLog trialLog = TrialLog.getTrialLog();
        trialLog.resetTrialLog();

        for (Trial trial : trials){
            trialLog.addTrial(trial);
        }

        onDoneGetTrialsListener.onDoneGetTrials(trialLog);
    }
    public ArrayList<Trial> getTrials(){
        return trials;
    }
    public void getTrial(String trialID, CrowdFlyListeners.OnDoneGetTrialListener onDoneGetTrialListener){
        Trial loopTrial = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < trials.size()){
            loopTrial = trials.get(i);
            if (loopTrial.getTrialID().matches(trialID)){
                loop = false;
            }
            i++;
        }
        if (loopTrial != null) {
            onDoneGetTrialListener.onDoneGetTrial(loopTrial);
        }
        else{
            Log.e("TrialController","Trial not found on find!");
        }

    }

    public void addTrialData(Trial trial, String experimentID) {
        trials.add(trial);
        trialsCollection.add(trial.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            trial.setTrialID(newId);
                            setTrialData(trial, experimentID);
                        }
                    }
                }
        );
    }


    public void setTrialData(Trial trial, String experimentID) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
    }

    public void removeTrialData(String trialID){
        Trial loopTrial = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < trials.size()){
            loopTrial = trials.get(i);
            if (loopTrial.getTrialID().matches(trialID)){
                loop = false;
            }

            i++;
        }
        if (loopTrial != null) {
            trials.remove(loopTrial);
        }
        else{
            Log.e("TrialController","Trial not found on delete!");
        }

        DocumentReference doc = trialsCollection.document(String.valueOf(trialID));
        Log.e("dd", doc.getId());
        doc.delete();
    }

    public void removeTrials(){
        for (Trial trial : trials){
            Log.e("deletion",trial.getTrialID());
            DocumentReference doc = trialsCollection.document(String.valueOf(trial.getTrialID()));
            doc.delete();
        }
    }


}
