package com.cmput301w21t06.crowdfly.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Barcode class
 * It contains Trial Information, Owner ID and a Code ID.
 */
public class Barcode {
    @DocumentId
    private String codeID;
    private Map<String, Object> trialData;
    private String ownerID;

    public Barcode(Map<String, Object> trialData, String ownerID) {
        this.trialData = trialData;
        this.ownerID = ownerID;
    }

    /***
     * This converts information from database to Barcode
     * @return Map
     */
    public Barcode(Map<String, Object> data) {
        this.ownerID = (String) data.get("ownerID");
        this.trialData = (Map<String, Object>) data.get("trial");
    }

    /**
     * Sets the ID of the Barcode
     * @param codeID
     */
    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public Map<String, Object> getTrialData() {
        return trialData;
    }

    /**
     * Gets the ID of the Barcode
     * @return
     */
    public String getCodeID() {
        return codeID;
    }

    /***
     * This transforms the Barcode to a HashMap that is stored in the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> code = new HashMap<>();
        code.put("ownerID", this.ownerID);
        code.put("trial", this.trialData);
        return code;
    }

}
