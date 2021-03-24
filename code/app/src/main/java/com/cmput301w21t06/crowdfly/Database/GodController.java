package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class GodController {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getDb(){
        return db;
    }

    public static void allmightySetup(){
        UserController.setUp();
    }

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
