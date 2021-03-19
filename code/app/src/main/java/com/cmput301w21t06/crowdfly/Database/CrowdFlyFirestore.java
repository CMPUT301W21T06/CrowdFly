package com.cmput301w21t06.crowdfly.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.SubscriptionManager;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
//import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class to contain all methods of interacting with Firestore
 */
public class  CrowdFlyFirestore {
    private final FirebaseFirestore firestoreInstance = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

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
    public void addExperimentData(Experiment experiment) {

        this.getCollectionReference(CrowdFlyFirestorePaths.experimentsCollection()).add(experiment.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            experiment.setExperimentId(newId);
                            CrowdFlyFirestore.this.setExperimentData(experiment);
                        }
                    }
                }
        );
    }

    public void getUsers(OnDoneGetIdsListener onDoneGetIdsListener){
        CollectionReference users = this.getCollectionReference("users");

        users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> userIds = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // get all data from each of the experiment documents
                                String id = document.getId().toString();
                                if (!id.matches("Admin")) {
                                    userIds.add(id);
                                }

                            }
                            onDoneGetIdsListener.onDoneGetIds(userIds);
                        } else {
                            Log.e("", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void setExperimentData(Experiment experiment) {
        this.setDocumentData(CrowdFlyFirestorePaths.experiment(experiment.getExperimentId()), experiment.toHashMap());
    }

    /**
     * Sets a subscribed user
     * @param experiment
     * @param user
     */
    public void setSubscribedUser(Experiment experiment, User user) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("subscribed", true);
        this.setDocumentData(CrowdFlyFirestorePaths.subscriptions(experiment.getExperimentId(), user.getUserID()),data);
    }

    /**
     * Removes a subscribed user
     * @param experiment
     * @param user
     */
    public void removeSubscribedUser(Experiment experiment, User user) {
        this.deleteDocumentData(CrowdFlyFirestorePaths.subscriptions(experiment.getExperimentId(),
                user.getUserID()));
    }

    /**
     * Checks if given user is subscribed to experiment
     * @param experiment
     * @param user
     */
    public void isSubscribed(Experiment experiment, User user, SubscriptionManager.OnDoneGetSubscribedListener OnDoneGetSubscribedListener) {
        DocumentReference doc = new CrowdFlyFirestore().getDocumentReference(CrowdFlyFirestorePaths.subscriptions(experiment.getExperimentId(),
                user.getUserID()));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    OnDoneGetSubscribedListener.onDoneGetIsSubscribed(doc.exists());
                }
                else {
                    OnDoneGetSubscribedListener.onDoneGetIsSubscribed(false);
                }
            }
        });
    }

    /***
     * Gets collection of experiments - ie. gets the full list of experiments from FireStore.
     * @param onDoneGetExpLogListener
     */
    public void getExperimentLogData(OnDoneGetExpLogListener onDoneGetExpLogListener) {
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
                    onDoneGetExpLogListener.onDoneGetExperiments(expLog);
                } else {
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Gets a single experiment
     * @param experimentId
     * @param onDoneGetExperiment
     */
    public void getExperimentData(String experimentId, OnDoneGetExpListener onDoneGetExperiment) {
        DocumentReference expData = this.getDocumentReference(CrowdFlyFirestorePaths.experiment(experimentId));

        expData.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Experiment experiment = new Experiment(value.getData());
                onDoneGetExperiment.onDoneGetExperiment(experiment);
            }
        });

    }

    /***
     * Adds a single new trial
     * @param trial
     * @param experimentID
     */
    public void addTrialData(Trial trial, String experimentID) {
        this.getCollectionReference(CrowdFlyFirestorePaths.trials(experimentID)).add(trial.toHashMap()).addOnCompleteListener(
                new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            String newId = task.getResult().getId();
                            trial.setTrialID(newId);
                            CrowdFlyFirestore.this.setTrialData(trial, experimentID);
                        }
                    }
                }
        );
    }

    /***
     * Set a single trial data
     * @param trial
     * @param experimentID
     */
   public void setTrialData(Trial trial, String experimentID) {
       this.setDocumentData(CrowdFlyFirestorePaths.trial(trial.getTrialID(), experimentID), trial.toHashMap());
   }

    /***
     * Get a whole collection of Trials
     * @param experimentID
     */
   public void getTrialData(String experimentID, OnDoneGetTrialsListener onDoneGetTrialsListener) {
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
                                if(data.get("successes") != null ){
                                    trialLog.addTrial(new BinomialTrial(data));
                                }else if(data.get("measurement") != null ){
                                    trialLog.addTrial(new MeasurementTrial(data));
                                }else if(data.get("count") != null ){
                                    trialLog.addTrial(new CountTrial(data));
                                }
                                //trialLog.addTrial(new Trial(data));
                            }
                            onDoneGetTrialsListener.onDoneGetTrials(trialLog);
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                   }
               });
   }
    /**
     * this gets a default profile pic for each user
     * @param onDoneGetProfilePicListener
     * */
   public void getProfilePic(OnDoneGetProfilePicListener onDoneGetProfilePicListener) {
        StorageReference imgRef = storage.getReferenceFromUrl("gs://crowdfly-76eb6.appspot.com/smiley.png");
        onDoneGetProfilePicListener.onDoneGetProfilePic(imgRef);
    }
    /**
     * this takes in an experiment ID and trial ID and references to the DB to create a trial
     * @return
     *      measurement trial
     * @param expID
     * @param trialID
     */
   public MeasurementTrial getMTrial(String expID, String trialID){
       DocumentReference trialRef = this.getDocumentReference(CrowdFlyFirestorePaths.trial(trialID, expID));
       final String[] mDescription = new String[1];
       final String[] mMeasurement = new String[1];
       trialRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               mMeasurement[0] = value.getString("measurement");
               mDescription[0] = value.getString("description");
           }
       });
       MeasurementTrial newMTrial = new MeasurementTrial(mDescription[0], mMeasurement[0]);
       return newMTrial;
   }

    /**
     * this takes in an experiment ID and trial ID and references to the DB to create a trial
     * @return
     *      count trial
     * @param expID
     * @param trialID
     */
    public CountTrial getCTrial(String expID, String trialID){
        DocumentReference trialRef = this.getDocumentReference(CrowdFlyFirestorePaths.trial(trialID, expID));
        final String[] cDescription = new String[1];
        final String[] cCount = new String[1];
        trialRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                cCount[0] = value.getString("count");
                cDescription[0] = value.getString("description");
            }
        });
        CountTrial newCTrial = new CountTrial(cDescription[0], cCount[0]);
        return newCTrial;
    }

    /**
     * this takes in an experiment ID and trial ID and references to the DB to create a trial
     * @return
     *      binomial trial
     * @param expID
     * @param trialID
     */
    public BinomialTrial getBTrial(String expID, String trialID){
        DocumentReference trialRef = this.getDocumentReference(CrowdFlyFirestorePaths.trial(trialID, expID));
        final String[] bDescription = new String[1];
        final String[] bFailures = new String[1];
        final String[] bSuccesses = new String[1];


        trialRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                bFailures[0] = value.getString("failures");
                bSuccesses[0] = value.getString("successes");
                bDescription[0] = value.getString("description");

            }
        });

        BinomialTrial newBTrial = new BinomialTrial(bDescription[0], bSuccesses[0], bFailures[0]);
        return newBTrial;
    }

    /**
     * this removes trial data according to the experiment ID And trial ID
     * @param expID
     * @param trialID
     */
    public void removeTrialData(String expID, String trialID){
        CollectionReference trialRef = this.getCollectionReference(CrowdFlyFirestorePaths.trials(expID));

        trialRef.document(String.valueOf(trialID)).delete();
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
     * Deletes document at given path
     * @param path
     */
    private void deleteDocumentData(String path) {
        firestoreInstance.document(path).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FIRESTORE", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FIRESTORE", "Doc deleted successfully");
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
    public DocumentReference getDocumentReference(String path) {
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
     *  Deletes an experiment
     * @param experimentId
     */

    public void deleteExperiment(String experimentId) {

        CollectionReference expData = this.getCollectionReference("Experiments");
        expData.document(String.valueOf(experimentId)).delete();
    }

    /**
     * Interface for listeners for when User is successfully retrieved
     */
    public interface OnDoneGetUserListener {
        public void onDoneGetUser(User user);
    }

    /***
     * Interface for retrieving the experiment log data
     */
    public interface OnDoneGetExpLogListener {
        public void onDoneGetExperiments(ExperimentLog expLog);
    }

    /***
     * Interface for retrieving a single experiment
     */
    public interface OnDoneGetExpListener {
        public void onDoneGetExperiment(Experiment experiment);
    }

    /***
     * Interface for retrieving the trial log data
     */
    public interface OnDoneGetTrialsListener {
        public void onDoneGetTrials(TrialLog trialList);
    }

    /***
     * Interface for retrieving the profile picture
     */
    public interface OnDoneGetProfilePicListener {
        public void onDoneGetProfilePic(StorageReference pic);
    }

    public interface OnDoneGetIdsListener {
        public void onDoneGetIds(ArrayList<String> ids);
    }


}