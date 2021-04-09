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
import com.google.firebase.firestore.DocumentSnapshot;

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
     * This instantiates a Code class and adds it to the users saved code collection
     * @param trial The trial results to generate as a Code
     * @param listener Callback method once code metadata is stored within firestore
     */
    public void registerOldCode(Trial trial, String codeID, OnDoneRegisteredCodeListener listener) {
        Barcode code = this.generateCode(trial);
        Map<String, Object> data = code.toHashMap();
        code.setCodeID(codeID);
        this.codesCollection.document(codeID).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onDoneRegisteredCode(code);
            }
        });
    }

    public void getBarcode(String codeID, OnDoneGetCodeListener listener) {
         codesCollection.document(codeID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Barcode code = new Barcode(null, null);
                code.setCodeID(codeID);
                if(task.isSuccessful()){
                    if(task.getResult().getData()!=null){
                        code = new Barcode(task.getResult().getData());
                    }
                } else {
                    Log.e("CODE CONTROLLER", "Code unable to be registered");
                }
                listener.OnDoneGetCodeListener(code);
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
     * Interface for view class to implement to get reference to the code object.
     */
    public interface OnDoneRegisteredCodeListener {
        void onDoneRegisteredCode(Barcode barcode);
    }

    /**
     * Interface for view class to implement to get reference to the stored code
     */
    public interface OnDoneGetCodeListener {
        void OnDoneGetCodeListener(Barcode barcode);
    }

}
