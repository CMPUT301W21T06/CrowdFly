package com.cmput301w21t06.crowdfly.Models;

import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * this is the Trial superclass that extends its functionalities to its subclasses
 */
public class Trial {
    protected String trialID;
    protected String description;
//    private Boolean locRequired;
    private String location = "";
//    private String result;
//    private Statistics statistics;
    protected String creatorID;

    public Trial(String description, String creatorID, String trialID) {
        this.description = description;
        this.creatorID = creatorID;
        this.trialID = trialID;
    }
    public Trial(Map<String, Object> data) {
        this.description = (String) data.get("description");
        this.trialID = (String) data.get("trialID");
        this.creatorID = (String) data.get("experimenter");
    }
    /**
     * this returns user id that is connected with a specific trial
     * @return userID
     */
    public String getExperimenterID(){return creatorID;}

    /***
     * this returns the trial ID number
     * @return creatorID
     */
    public String getTrialID() {
        return trialID;
    }

    /***
     * this sets the trial ID number
     * @param trialID
     */
    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    /***
     * this sets the description of the trial
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * this sets the user ID to a specific trial class
     * @param userID
     */
    public void setExperimenterID(String userID){ this.creatorID = userID;}
    /***
     * this gets the description of the trial
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /***
     * this returns an instance of the Trial object in its current state
     * @return Map
     */
    public Trial getData(){
        return new Trial(description, creatorID, trialID);
    }

    public String getLocation() {return location;}


    /***
     * this transforms the Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        trl.put("trialID",this.trialID);
        trl.put("experimenter",creatorID);
        return trl;
    }

}

