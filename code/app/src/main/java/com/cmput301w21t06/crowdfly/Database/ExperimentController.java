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
import java.util.Map;

public class ExperimentController {
    private static CollectionReference experimentCollection = GodController.getDb().collection("Experiments");
    private static ArrayList<Experiment> experiments = new ArrayList<Experiment>();

    public static void setUp(){
        experimentCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                experiments.clear();
                Log.e("FF","FUCK14");
                for (QueryDocumentSnapshot doc : response){
                    experiments.add(new Experiment(doc.getData()));
                }
                Log.e("FF",String.valueOf(experiments));
            }
        });
    }


    public static void getExperimentLogData(CrowdFlyListeners.OnDoneGetExpLogListener onDoneGetExpLogListener) {
        ExperimentLog expLog = ExperimentLog.getExperimentLog();
        expLog.resetExperimentLog();
        for (Experiment exp : experiments){
            expLog.addExperiment(exp);
        }
        onDoneGetExpLogListener.onDoneGetExperiments();
    }

    public static void getExperimentData(String experimentId, CrowdFlyListeners.OnDoneGetExpListener onDoneGetExperimentListener) {
        Experiment exp = null;
        boolean loop = true;
        int i = 0;
        while (loop){
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

    public static void setExperimentData(Experiment experiment) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.experiment(experiment.getExperimentId()), experiment.toHashMap());
    }

    public static void deleteExperiment(String experimentId, Experiment exp) {
        Log.d("FF",String.valueOf(experiments));
        experiments.remove(exp);
        Log.d("FF",String.valueOf(experiments));

        DocumentReference doc = experimentCollection.document(String.valueOf(experimentId));
        doc.delete();
    }

}
