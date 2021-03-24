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
    private static ArrayList<User> users = new ArrayList<User>();

    /**
     * This handles the set up of a snapshot listener by the GodController
     */
    public static void setUp() {
        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                users.clear();
                for (QueryDocumentSnapshot doc : response){
                    users.add(new User(doc.getData()));
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
        for (User user : users){
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
        User user = null;
        boolean loop = true;
        int i = 0;
        while (loop){
            user = users.get(i);
            if (user.getUserID().matches(uid)){
                loop = false;
            }
            i++;
        }
        if (user != null) {
            onDoneGetUserListener.onDoneGetUser(user);
        }
        else{
            Log.e("UserController","User not found!");
        }

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
