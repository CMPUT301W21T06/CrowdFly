package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class controls all operations related to experiments
 */
public class ExperimentController {
    private static CollectionReference experimentCollection = GodController.getDb().collection("Experiments");
    private static ArrayList<Experiment> experiments = new ArrayList<Experiment>();

    /**
     * This sets up the snapshot listener for experiments
     */
    public static void setUp(){
        experimentCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                experiments.clear();
                Log.e("FF","FUCK25");
                for (QueryDocumentSnapshot doc : response){
                    Experiment exp = new Experiment(doc.getData());
                    experiments.add(exp);
                }
                Log.e("FF",String.valueOf(experiments));
            }
        });
    }


    /**
     * This feeds all the experimeents into a new experiment log
     * @param onDoneGetExpLogListener
     * The class that implemeents the method to handle the result of this function
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
                            experiment.setExperimentId(newId);
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
        Experiment loop_exp = null;
        boolean loop = true;
        int i = 0;
        while (loop && i < experiments.size()){
            loop_exp = experiments.get(i);
            if (loop_exp.getExperimentId().matches(experimentId)){
                loop = false;
            }
            i++;
        }
        if (loop_exp != null) {
            experiments.remove(loop_exp);
        }
        else{
            Log.e("ExpController","Exp not found!");
        }
        Log.e("Item GOne",String.valueOf(experiments));
        GodController.deleteDocumentData(CrowdFlyFirestorePaths.experiment(experimentId));
    }

}
