package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class to contain all methods of interacting with Firestore
 */
public class CrowdFlyFirestore {
    private final FirebaseFirestore firestoreInstance = FirebaseFirestore.getInstance();

    // not too sure why we need userID?
    //    private String userID;
//    public CrowdFlyFirestore(String userID){
//        this.userID = userID;
//    }

    public CrowdFlyFirestore() {}

    /**
     * Sets user profile to HashMap representation of instance of User class
     * @param user
     */
    public void setUserProfile(@NonNull User user) {
        this.setDocumentData(CrowdFlyFirestorePaths.userProfile(user.getUserID()), user.toHashMap());
    }

    /**
     * Create new user by getting the global user counter and incrementing it once its assigned.
     *
     * This method should only be used once when creating the user. Use set profile to update.
     * @param user
     */
    public void createUserProfile(@NonNull User user) {

        this.getDocumentReference(CrowdFlyFirestorePaths.displayId()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    String displayID = documentSnapshot.getData().get("place").toString();
                    user.setDisplayID(displayID);
                    CrowdFlyFirestore.this.setUserProfile(user);

                    // Increment display ID counter here
                    Map<String, Object> counter = new HashMap<>();
                    counter.put("place", FieldValue.increment(1));
                    CrowdFlyFirestore.this.updateDocumentData(CrowdFlyFirestorePaths.displayId(),
                            counter);
                }
                catch (Exception e){
                    Log.e("CREATE USER PROFILE", e.getMessage());
                }
            }
        });
    }

    /**
     * Get User object for provided userID and invokes the OnDoneListener
     * @param userID
     */
    public void getUserProfile(@NonNull String userID, OnDoneGetUserListener onDoneGetUserListener) {
        DocumentReference userData = this.getDocumentReference(CrowdFlyFirestorePaths.userProfile(userID));

        userData.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = new User(value.getData());
                onDoneGetUserListener.onDoneGetUser(user);
            }
        });
    }

    /***
     * Set or Add data for a single experiment
     * @param experiment
     */
    public void setExperimentData(Experiment experiment) {
        this.setDocumentData(CrowdFlyFirestorePaths.experiments(experiment.getExperimentId()), experiment.toHashMap());
    }

    /***
     * Gets collection of experiments - ie. gets the full list of experiments from FireStore.
     */
    public void getExperimentData(OnDoneGetExpListener onDoneGetExpListener) {
        CollectionReference expData = this.getCollectionReference("Experiments");
        ExperimentLog expLog = ExperimentLog.getExperimentLog();
        expLog.resetExperimentLog();

        expData
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // get all data from each of the experiment documents
                        Map data = document.getData();
                        expLog.addExperiment(new Experiment(data));
                    }
                    onDoneGetExpListener.onDoneGetExperiments(expLog);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /***
     * Set a single trial data
     * @param trial
     * @param experimentID
     */
   public void setBinomialTrialData(BinomialTrial trial, int experimentID) {
       this.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
   }
    /***
     * Set a single trial data
     * @param trial
     * @param experimentID
     */
    public void setCountTrialData(CountTrial trial, int experimentID) {
        this.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
    }
    /***
     * Set a single trial data
     * @param trial
     * @param experimentID
     */
    public void setMeasurementTrialData(MeasurementTrial trial, int experimentID) {
        this.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
    }

    /***
     * Get a whole collection of Trials
     * @param experimentID
     */
   public void getTrialData(int experimentID, OnDoneGetTrialsListener onDoneGetTrialsListener) {
       CollectionReference trialData = this.getCollectionReference(CrowdFlyFirestorePaths.trials(experimentID));
       TrialLog trialLog = TrialLog.getTrialLog();
       trialLog.resetTrialLog();

       trialData
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map data = document.getData();
                                trialLog.addTrial(new Trial(data));
                            }
                            onDoneGetTrialsListener.onDoneGetTrials(trialLog);
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                   }
               });
   }

    /**
     * Sets document at given path
     * @param path
     * @param data
     */
    private void setDocumentData(String path, Map<String, Object> data) {
        data.put("lastUpdatedAt", FieldValue.serverTimestamp()); // Adds a server timestamp for all updates

        firestoreInstance.document(path).set(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FIRESTORE", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FIRESTORE", "Data set successfully");
            }
        });
    }

    /**
     * Updates document at given path
     * @param path
     * @param data
     */
    private void updateDocumentData(String path, Map<String, Object> data) {
        data.put("lastUpdatedAt", FieldValue.serverTimestamp()); // Adds a server timestamp for all updates

        firestoreInstance.document(path).update(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FIRESTORE", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FIRESTORE", "Data set successfully");
            }
        });
    }

    /**
     * Gets a reference to document at given path
     * @param path
     * @return
     */
    private DocumentReference getDocumentReference(String path) {
        return firestoreInstance.document(path);
    }

    /**
     * Gets reference to collection at given path
     * @param path
     * @return
     */
    private CollectionReference getCollectionReference(String path) {
        return firestoreInstance.collection(path);
    }

    /**
     * Interface for listeners for when User is successfully retrieved
     */
    public interface OnDoneGetUserListener {
        public void onDoneGetUser(User user);
    }

    /***
     *
     */
    public interface OnDoneGetExpListener {
        public void onDoneGetExperiments(ExperimentLog expLog);
    }

    /***
     *
     */
    public interface OnDoneGetTrialsListener {
        public void onDoneGetTrials(TrialLog trialList);
    }
}