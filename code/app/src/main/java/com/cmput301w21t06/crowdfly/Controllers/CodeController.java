package com.cmput301w21t06.crowdfly.Controllers;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w21t06.crowdfly.Models.Barcode;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

/**
 * This abstract controller contains the framework for operations related to Barcode and QRCodes
 */
public abstract class CodeController {
    protected CollectionReference codesCollection;
    protected String userID;
    public CodeController(CollectionReference codesCollection, String userID) {
        this.userID = userID;
        this.codesCollection = codesCollection;
    }

    /**
     * Instantiates a Barcode class with trial information
     * @param trial
     * @return
     */
    public abstract Barcode generateCode(Trial trial);

    /**
     * This instantiates a Code class and adds it to the users saved code collection
     * @param trial The trial results to generate as a Code
     * @param listener Callback method once code metadata is stored within firestore
     */
    public void registerCode(Trial trial, OnDoneRegisteredCodeListener listener) {
        Barcode code = this.generateCode(trial);
        Map<String, Object> data = code.toHashMap();
        this.codesCollection.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    String codeID = task.getResult().getId();
                    code.setCodeID(codeID);
                    listener.onDoneRegisteredCode(code);
                } else {
                    Log.e("CODE CONTROLLER", "Code unable to be registered");
                }
            }
        });
    }

    /**
     * This method is responsible for displaying the code
     * @param barcode
     * @return
     */
    public Bitmap printCode(Barcode barcode){
        return null;
    };

    /**
     * Save code bitmap at the given path
     * @param bitmap
     * @param path
     * @return
     */
    public Boolean saveCode(Bitmap bitmap, String path){
        return false;
    }

    /**
     * Interface for view class to implement to get reference to the code object.
     */
    public interface OnDoneRegisteredCodeListener {
        void onDoneRegisteredCode(Barcode barcode);
    }
}
