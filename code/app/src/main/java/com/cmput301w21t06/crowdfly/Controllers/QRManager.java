package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.QRCode;

import java.util.ArrayList;

/**
 * This will manage operations related to a QR code
 * Nothing related to QR codes has been implemented
 */
public class QRManager {
    private Experiment exp;
    private QRCode code;
    private String qrType;

    public ArrayList<QRCode> generateQR(String qrType){
        return null;
    }
    public void printQR(){}
    private void scanHandler(){}
}
