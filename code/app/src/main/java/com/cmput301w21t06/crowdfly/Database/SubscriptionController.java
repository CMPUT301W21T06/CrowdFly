package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * This class controls all operations related to subscriptions
 */
public class SubscriptionController {
    private CollectionReference subsCollection;
//    private HashMap<String, User> subs = new HashMap<String, User>();
    public SubscriptionController(String eid){
        subsCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.subscriptions(eid));
//        setUp();
    }

    /**
     * This sets up the snapshot listener for subscriptions
     */
//    private void setUp(){
//        subsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
//                subs.clear();
//                for (QueryDocumentSnapshot doc : response){
//                    Log.e("ddd",String.valueOf(doc.getData()));
//                    User user = new User(doc.getId());
//                    subs.put(user.getUserID(),user);
//                }
//            }
//        });
//    }
    /**
     * This subscribes a user to an experiment
     * @param experiment
     * The experiment id to subscribe a user to
     * @param user
     * The user we are subscribing to an experiment
     */
    public void setSubscribedUser(Experiment experiment, User user) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("subscribed", true);
        GodController.setDocumentData(CrowdFlyFirestorePaths.subscription(experiment.getExperimentId(), user.getUserID()),data);
    }

    /**
     * This unsubscribes a user from an experiment
     * @param experiment
     * the experiment id to unsubscribe a user from
     * @param user
     * the user we are unsubscribing from an experiment
     */
    public void removeSubscribedUser(Experiment experiment, User user) {
        GodController.deleteDocumentData(CrowdFlyFirestorePaths.subscription(experiment.getExperimentId(), user.getUserID()));
    }

    /**
     * This checks if a user is subscribed to an experiment
     * @param user
     * The user we are checking to see if they are subscribed to an experiment
     * @param OnDoneGetSubscribedListener
     * The listener that listens to the to the completion of the query request and returns a boolean to
     */
    public void isSubscribed(User user, CrowdFlyListeners.OnDoneGetSubscribedListener OnDoneGetSubscribedListener) {
        DocumentReference doc = subsCollection.document(String.valueOf(user.getUserID()));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    OnDoneGetSubscribedListener.onDoneGetIsSubscribed(doc.exists());
                } else {
                    OnDoneGetSubscribedListener.onDoneGetIsSubscribed(false);
                }
            }
        });
    }

    /**
     * This removes a subscription document from an experiment from the database
     */
    public void removeSubs(){
        subsCollection
                .whereEqualTo("shit",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("stuff", document.getId() + "=>" + document.getData());
                            }
                        } else {
                            Log.d("Error get documents:", String.valueOf(task.getException()));
                        }
                    }
                });
        // change this loop
//        for (String subId : subs.keySet()){
        DocumentReference doc = subsCollection.document("here");
        doc.delete();
//        }
    }

}
