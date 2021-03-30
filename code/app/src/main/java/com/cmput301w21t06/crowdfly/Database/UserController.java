package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w21t06.crowdfly.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This handles almost all operations for users - does not include the subscription aspect of users
 */
//db setters should process shit in the class too
public class UserController {
    private static CollectionReference userCollection = GodController.getDb().collection(CrowdFlyFirestorePaths.users());
    private static HashMap<String, User> users = new HashMap<String, User>();
    private static HashMap<String,String> converter = new HashMap<String, String>();
    /**
     * This handles the set up of a snapshot listener by the GodController
     */
    public static void setUp() {
        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot response, @Nullable FirebaseFirestoreException error) {
                users.clear();
                converter.clear();
                if(response != null){
                    for (QueryDocumentSnapshot doc : response){
                        User user = new User(doc.getData());
                        users.put(user.getUserID(),user);
                        converter.put(user.getDisplayID(),user.getUserID());
                    }
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
        for (String id : converter.keySet()){
            ids.add(id);
        }
        Log.e("sleep",String.valueOf(ids));
        onDoneGetIdsListener.onDoneGetIds(ids);
    }

    /**
     * This finds a particular user by their display id
     * @param did
     * The user's display id we are searching for
     * @param onDoneGetUserListener
     * The class that implements the onDone listener
     */
    public static void getUserProfile(String did, CrowdFlyListeners.OnDoneGetUserListener onDoneGetUserListener){
        Log.e("sleep",String.valueOf(did));
        onDoneGetUserListener.onDoneGetUser(users.get(converter.get(did)));
    }

    public static String reverseConvert(String uid){
        for (String k : converter.keySet()){
            if (converter.get(k).matches(uid)){
                return k;
            }
        }
        return "ERROR";
    }

    /**
     * This faciliates the creation of a new user in the database
     * @param user
     * This is the user object that is partially created locally
     */
    public static void addUserProfile(@NonNull User user){
        GodController.getDb().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference doc = GodController.getDb().document(CrowdFlyFirestorePaths.counter());
                Long place = transaction.get(doc).getLong("counter");
                Long did = place;
                user.setDisplayID(String.valueOf(did));
                transaction.update(doc,"counter",place + 1);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("User transaction","Transaction successful!");
                setUserProfile(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("User transaction","Transaction failed!");
            }
        });
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
