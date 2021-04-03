package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Models.Barcode;
import com.cmput301w21t06.crowdfly.Models.QRCode;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.google.firebase.firestore.CollectionReference;

public class BarcodeController extends CodeController {
    public BarcodeController(CollectionReference codesCollection, String userID) {
        super(codesCollection, userID);
    }

    @Override
    public Barcode generateCode(Trial trial) {
        return new Barcode(trial.toHashMap(), this.userID);
    }
}
