package com.cmput301w21t06.crowdfly.Models;

import java.util.Map;

/**
 * This is the QR class, nothing has been implemented with regards to this
 */
public class QRCode extends Barcode {

    public QRCode(Map<String, Object> trialData, String ownerID) {
        super(trialData, ownerID);
    }

    public QRCode(Map<String, Object> data) {
        super(data);
    }
}
