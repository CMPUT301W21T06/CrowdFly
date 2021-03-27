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
public class Trial implements Comparable<Trial> {
    protected String trialID;
    protected String description;
//    private Boolean locRequired;
//    private String location;
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
    public String getExperimenterID(){return creatorID;}

    public String getTrialID() {
        return trialID;
    }

    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setExperimenterID(String userID){ this.creatorID = userID;}
    public String getDescription() {
        return description;
    }

    public Trial getData(){
        return new Trial(description, creatorID, trialID);
    }


    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        trl.put("trialID",this.trialID);
        trl.put("experimenter",creatorID);
        return trl;
    }

    @Override
    public int compareTo(Trial trial) {
        return this.getTrialID().compareTo(trial.getTrialID());
    }


}

