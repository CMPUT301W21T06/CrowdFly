package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Map;

/**
 * This sets up all controllers and provides access to data needed by all controllers to function
 */
public class GodController {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Boolean isEmulated;

    /**
     * This provides access to the database instance
     * @return
     * The database instance
     */
    public static FirebaseFirestore getDb(){
        return db;
    }

    public static Boolean useEmulator(){
        if(isEmulated == null){
            isEmulated = true;
            FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
            db.useEmulator("10.0.2.2", 8080);

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            db.setFirestoreSettings(settings);
        }
        return isEmulated;
    }

    /**
     * This sets up all other controllers
     */
    public static void allmightySetup(){
        ExperimentController.setUp();
        UserController.setUp();
    }

    /**
     * This adds a timestamp to the stored database item
     * @param path
     * This is the path to the document that will be modified
     * @param data
     * Item to be stored, timestamp added to item and then it is stored
     */

    public static void setDocumentData(String path, Map<String, Object> data) {
        data.put("lastUpdatedAt", FieldValue.serverTimestamp()); // Adds a server timestamp for all updates

        db.document(path).set(data).addOnFailureListener(new OnFailureListener() {
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
     * This deletes a document at a given path, but provides a completion listener
     * @param path
     * Path to document
     */
    public static void deleteDocumentData(String path) {
        db.document(path).delete().addOnFailureListener(new OnFailureListener() {
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



}
