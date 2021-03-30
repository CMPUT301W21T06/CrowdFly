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
/**
 * This class controls all operations related to Trials
 */
public class TrialController {
    private CollectionReference trialsCollection;
    private ArrayList<Trial> trials = new ArrayList<Trial>();
    public TrialController(String eid) {
        trialsCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.trials(eid));
        setUp();
    }
    /**
     * This sets up the snapshot listener for trials
     */
    private void setUp(){
        trialsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                trials.clear();
                if(response != null){
                    for (QueryDocumentSnapshot doc : response){
                        String type = doc.getString("type");
                        Trial trial = null;
                        switch (type) {
                            case "binomial":
                                trial = new BinomialTrial(doc.getData());
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
            }
        });
    }
    /**
     * This feeds all the experiments into a new trial log
     * @param onDoneGetTrialsListener
     * The class that implements the method to handle the result of this function
     */
    public void getTrialLogData(CrowdFlyListeners.OnDoneGetTrialsListener onDoneGetTrialsListener){
        Collections.sort(trials);
        TrialLog trialLog = TrialLog.getTrialLog();
        trialLog.resetTrialLog();

        for (Trial trial : trials){
            trialLog.addTrial(trial);
        }

        onDoneGetTrialsListener.onDoneGetTrials(trialLog);
    }
    /**
     * Returns all trials from Array List
     * @return trials
     * this returns all trials from the ArrayList<Trial>
     */
    public ArrayList<Trial> getTrials(){
        return trials;
    }

    /**
     * This gets a specific trial
     * @param trialID
     * The trial id to look for
     * @param onDoneGetTrialListener
     * The class that implements a handler for the result of this method
     */
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
    /**
     * This creates a new document in the database for the trial with the necessary data
     * @param trial
     * The trial to store
     * @param experimentID
     * The experiment to reference to get the location of where to store the trial
     */
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

    /**
     * This proceeds to store the trial id and timestamp with the new trial
     * Can/should be used to update data of an existing trial
     * @param trial
     * This is the manipulated trial
     * @param experimentID
     * Experiment ID used to grab the trial path
     */
    public void setTrialData(Trial trial, String experimentID) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
    }
    /**
     * This proceeds to remove an existing trial within an experiment
     * @param trialID
     * This is the trial to be removed
     */
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
        doc.delete();
    }
    /**
     * This deletes any and all trials
     */
    public void removeTrials(){
        for (Trial trial : trials){
            Log.e("deletion",trial.getTrialID());
            DocumentReference doc = trialsCollection.document(String.valueOf(trial.getTrialID()));
            doc.delete();
        }
    }


}
