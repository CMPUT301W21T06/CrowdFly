package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class controls all operations related to experiments
 */
public class ExperimentController {
    private static CollectionReference experimentCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.experiments());
    private static ArrayList<Experiment> experiments = new ArrayList<Experiment>();

    /**
     * This sets up the snapshot listener for experiments
     */
    public static void setUp(){
        Log.e("Fu","cK");
        experimentCollection.orderBy("lastUpdatedAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                experiments.clear();
                if(response != null){
                    for (QueryDocumentSnapshot doc : response){
                        Experiment exp = new Experiment(doc.getData());
                        exp.setUpFullExperiment(exp.getExperimentId());
                        experiments.add(exp);
                    }
                }
            }
        });
    }


    /**
     * This feeds all the experiments into a new experiment log
     * @param onDoneGetExpLogListener
     * The class that implements the method to handle the result of this function
     */
    public static void getExperimentLogData(CrowdFlyListeners.OnDoneGetExpLogListener onDoneGetExpLogListener) {
        ExperimentLog expLog = ExperimentLog.getExperimentLog();
        expLog.resetExperimentLog();
        for (Experiment exp : experiments){
            Log.e("Getting",String.valueOf(exp));
            expLog.addExperiment(exp);
        }
        onDoneGetExpLogListener.onDoneGetExperiments();
    }

    /**
     * This gets a specific experiment
     * @param experimentId
     * The experiment id to look for
     * @param onDoneGetExperimentListener
     * The class that implements a handler for the result of this method
     */
    public static void getExperimentData(String experimentId, CrowdFlyListeners.OnDoneGetExpListener onDoneGetExperimentListener) {
        Experiment exp = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < experiments.size()){
            exp = experiments.get(i);
            if (exp.getExperimentId().matches(experimentId)){
                loop = false;
            }
            i++;
        }
        if (exp != null) {
            onDoneGetExperimentListener.onDoneGetExperiment(exp);
        }
        else{
            Log.e("ExpController","Exp not found!");
        }
    }

    /**
     * This creates a new document in the database for the experiment with the necessary data
     * @param experiment
     * The experiment to store
     */
    public static void addExperimentData(Experiment experiment) {
        experiments.add(experiment);
        experimentCollection.add(experiment.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            experiment.setUpFullExperiment(newId);
                            setExperimentData(experiment);
                        }
                    }
                }
        );
    }

    /**
     * This proceeds to store the experiment id and timestamp with the new experiment
     * Can/should be used to update data of an existing experiment
     * @param experiment
     * This is the manipulated experiment
     */
    public static void setExperimentData(Experiment experiment) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.experiment(experiment.getExperimentId()), experiment.toHashMap());
    }

    /**
     * This deletes an experiment
     * @param experimentId
     * The experiment ID string
     * @param exp
     * The experiment object
     */
    public static void deleteExperiment(String experimentId, Experiment exp) {
        Experiment loopExp = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < experiments.size()){
            loopExp = experiments.get(i);
            if (loopExp.getExperimentId().matches(experimentId)){
                loop = false;
            }
            i++;
        }
        if (loopExp != null) {
            experiments.remove(loopExp);
        }
        else{
            Log.e("ExpController","Exp not found!");
        }
        loopExp.getTrialController().removeTrials();
        loopExp.getSubController().removeSubs();
        GodController.deleteDocumentData(CrowdFlyFirestorePaths.experiment(experimentId));
    }



}
