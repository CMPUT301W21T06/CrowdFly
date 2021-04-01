package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Models.Barcode;
import com.cmput301w21t06.crowdfly.Models.QRCode;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.google.firebase.firestore.CollectionReference;

/**
 * This will manage operations related to Barcode and QRCodes
 * Nothing related to QR codes has been implemented
 */
public abstract class CodeController {
    private CollectionReference codesCollection;

    public CodeController(CollectionReference codesCollection){
        codesCollection = this.codesCollection;
    }

    public Barcode generateCode(Trial trial){
        Barcode barcode = null;
        return barcode;
    }

    public Barcode registerCode(Trial trial){
        Barcode barcode = null;
        return barcode;
    }

    public abstract void scanCode(Barcode barcode);
    public abstract void printCode(Barcode barcode);
}
