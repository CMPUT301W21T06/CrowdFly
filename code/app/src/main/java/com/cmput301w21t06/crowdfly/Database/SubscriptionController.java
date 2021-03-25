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

public class SubscriptionController {
    private CollectionReference subsCollection;
    private HashMap<String, User> subs = new HashMap<String, User>();
    public SubscriptionController(String eid){
        subsCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.subscriptions(eid));
        setUp();
    }

    private void setUp(){
        subsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                subs.clear();
                for (QueryDocumentSnapshot doc : response){
                    User user = new User(doc.getData());
                    subs.put(user.getUserID(),user);
                }
            }
        });
    }

    public void setSubscribedUser(Experiment experiment, User user) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("subscribed", true);
        GodController.setDocumentData(CrowdFlyFirestorePaths.subscription(experiment.getExperimentId(), user.getUserID()),data);
    }


    public void removeSubscribedUser(Experiment experiment, User user) {
        GodController.deleteDocumentData(CrowdFlyFirestorePaths.subscription(experiment.getExperimentId(), user.getUserID()));
    }


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

    public void removeSubs(){
        for (String subId : subs.keySet()){
            DocumentReference doc = subsCollection.document(String.valueOf(subId));
            doc.delete();
        }
    }

}
