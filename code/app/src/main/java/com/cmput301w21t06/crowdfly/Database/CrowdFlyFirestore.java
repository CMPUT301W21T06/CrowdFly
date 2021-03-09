package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w21t06.crowdfly.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Main class to contain all methods of interacting with Firestore
 */
public class CrowdFlyFirestore {
    private final FirebaseFirestore firestoreInstance = FirebaseFirestore.getInstance();
    private String userID;
    public CrowdFlyFirestore(String userID){
        this.userID = userID;
    }

    /**
     * Sets user profile to HashMap representation of instance of User class
     * @param user
     */
    public void setUserProfile(@NonNull User user) {
        this.setDocumentData(CrowdFlyFirestorePaths.userProfile(userID), user.toHashMap());
    }

    /**
     * Get User object for provided userID and invokes the OnDoneListener
     * @param userID
     */
    public void getUserProfile(@NonNull String userID, OnDoneGetUserListener onDoneGetUserListener) {
        DocumentReference userData = this.getDocumentReference(CrowdFlyFirestorePaths.userProfile(userID));
        userData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = new User(documentSnapshot.getData());
                onDoneGetUserListener.onDoneGetUser(user);
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

}