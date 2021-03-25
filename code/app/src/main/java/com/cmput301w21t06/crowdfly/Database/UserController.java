package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//db setters should process shit in the class too
public class UserController {
    private static CollectionReference userCollection = GodController.getDb().collection("users");
    private static HashMap<String, User> users = new HashMap<String, User>();

    /**
     * This handles the set up of a snapshot listener by the GodController
     */
    public static void setUp() {
        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                users.clear();
                for (QueryDocumentSnapshot doc : response){
                    User user = new User(doc.getData());
                    users.put(user.getUserID(),user);
                }
            }
        });
    }

    /**
     * This gets all user ids of the current users
     * @param onDoneGetIdsListener
     * This is a reference to the class that implements the listener
     */
    public static void getUsers(CrowdFlyListeners.OnDoneGetIdsListener onDoneGetIdsListener) {
        ArrayList<String> ids = new ArrayList<String>();
        for (User user : users.values()){
            ids.add(user.getUserID());
        }
        onDoneGetIdsListener.onDoneGetIds(ids);
    }

    /**
     * This finds a particular user by their id
     * @param uid
     * The user id we are searching for
     * @param onDoneGetUserListener
     * The class that implements the onDone listener
     */
    public static void getUserProfile(String uid, CrowdFlyListeners.OnDoneGetUserListener onDoneGetUserListener){
        onDoneGetUserListener.onDoneGetUser(users.get(uid));


    }

    /**
     * Sets up a user profile in the database
     * @param user
     * The user to add to the database
     */
    public static void setUserProfile(@NonNull User user) {
        GodController.setDocumentData(CrowdFlyFirestorePaths.userProfile(user.getUserID()), user.toHashMap());
    }


}
