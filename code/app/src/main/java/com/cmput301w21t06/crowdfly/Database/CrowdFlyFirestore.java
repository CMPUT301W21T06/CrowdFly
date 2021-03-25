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

    /**
     * Sets a subscribed user
     * @param experiment
     * @param user
     */
    public void setSubscribedUser(Experiment experiment, User user) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("subscribed", true);
        GodController.setDocumentData(CrowdFlyFirestorePaths.subscriptions(experiment.getExperimentId(), user.getUserID()),data);
    }

    /**
     * Removes a subscribed user
     * @param experiment
     * @param user
     */
    public void removeSubscribedUser(Experiment experiment, User user) {
        GodController.deleteDocumentData(CrowdFlyFirestorePaths.subscriptions(experiment.getExperimentId(),
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




}